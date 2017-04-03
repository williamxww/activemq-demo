package com.bow.spring;

import com.bow.annotation.Consumer;
import com.bow.annotation.Listener;
import com.bow.mq.Event;
import com.bow.mq.MessageListenerImpl;
import com.bow.mq.RepeatMessageHandler;
import com.google.common.collect.Lists;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.RedeliveryPolicy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.Executor;

public class MessageConsumerBeanPostProcessor
        implements BeanPostProcessor, Ordered, BeanFactoryAware, SmartInitializingSingleton {

    private Object primitiveBean;

    private BeanFactory beanFactory;

    /**
     * ActiveMQ连接工厂
     */
    private ActiveMQConnectionFactory connectionFactory;

    /**
     * 线程池
     */
    private Executor executor;

    /**
     * 消息幂等处理
     */
    private RepeatMessageHandler repeatMsgHandler;

    /**
     * 消费者集合
     */
    private List<Object> consumerBeans = Lists.newArrayList();

    public MessageConsumerBeanPostProcessor() {
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        this.primitiveBean = bean;
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 获取标注@Consumer的注解
        Consumer consumer = bean.getClass().getAnnotation(Consumer.class);
        // bean上注解@Consumer
        if (consumer != null) {
            if (AopUtils.isAopProxy(bean)) {
                consumerBeans.add(primitiveBean);
            } else {
                consumerBeans.add(bean);
            }
        }
        return bean;
    }

    /**
     * 所有bean创建完成后处理消息消费者 {@link SmartInitializingSingleton}
     */
    public void afterSingletonsInstantiated() {
        connectionFactory = beanFactory.getBean(ActiveMQConnectionFactory.class);
        executor = beanFactory.getBean(Executor.class);
        repeatMsgHandler = beanFactory.getBean(RepeatMessageHandler.class);
        try {
            for (Object consumerBean : consumerBeans) {
                // 处理消费者监听器
                register(consumerBean);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 注册消息队列监听器
     *
     * @param bean
     */
    private void register(Object bean) throws Exception {
        Class<?> clazz = bean.getClass();
        // 获取@Listener注解的方法
        for (Method method : clazz.getDeclaredMethods()) {
            Class<?>[] types = method.getParameterTypes();
            // 方法参数个数必须为1，并且参数的类型为Event的子类
            if (types.length != 1 || !Event.class.isAssignableFrom(types[0])) {
                continue;
            }
            // 获取消息处理方法
            Listener listener = method.getAnnotation(Listener.class);
            // 创建连接
            Connection connection = connectionFactory.createConnection();
            RedeliveryPolicy policy = ((ActiveMQConnection) connection).getRedeliveryPolicy();
            // 设置重试策略
            policy.setInitialRedeliveryDelay(1000);
            policy.setBackOffMultiplier(2);
            policy.setUseExponentialBackOff(true);
            policy.setMaximumRedeliveries(2);
            // 启动连接
            connection.start();
            // 创建会话
            Session session = connection.createSession(listener.transaction(), ActiveMQSession.AUTO_ACKNOWLEDGE);
            // 创建队列
            Destination queue = session.createQueue(listener.topic());
            // 创建消费者
            MessageConsumer consumer = session.createConsumer(queue);
            // 创建消息处理类
            MessageListenerImpl messageListener = new MessageListenerImpl().setBean(bean)
                    .setDestination(listener.topic()).setExecutor(executor).setMethod(method).setN2(listener.n2())
                    .setSession(session).setTransaction(listener.transaction())
                    .setRepeatMessageHandler(repeatMsgHandler);
            // 设置消息监听器
            consumer.setMessageListener(messageListener);
        }
    }

    /**
     * {@link Ordered}
     * @return
     */
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }

    /**
     * {@link BeanFactoryAware}
     * 
     * @param beanFactory
     * @throws BeansException
     */
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}

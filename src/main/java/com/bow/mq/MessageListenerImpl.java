package com.bow.mq;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;

import com.bow.annotation.Consumer;
import com.bow.annotation.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

public class MessageListenerImpl implements MessageListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListenerImpl.class);

    /**
     * {@link Consumer} 注解的对象
     */
    private Object bean;

    /**
     * {@link Listener} 注解的方法
     */
    private Method method;

    /**
     * broker连接session
     */
    private Session session;

    /**
     * 消息投递地址
     */
    private String destination;

    /**
     * 消费消息的线程池
     */
    private Executor executor;

    /**
     * 是否支持事务
     */
    private boolean transaction;

    /**
     * 是否是n2级别的消息
     */
    private boolean n2;

    /**
     * 消息幂等处理
     */
    private RepeatMessageHandler repeatMessageHandler;

    public MessageListenerImpl() {
    }

    public RepeatMessageHandler getRepeatMessageHandler() {
        return repeatMessageHandler;
    }

    public MessageListenerImpl setRepeatMessageHandler(RepeatMessageHandler repeatMessageHandler) {
        this.repeatMessageHandler = repeatMessageHandler;
        return this;
    }

    public boolean isTransaction() {
        return transaction;
    }

    public MessageListenerImpl setTransaction(boolean transaction) {
        this.transaction = transaction;
        return this;
    }

    public Session getSession() {
        return session;
    }

    public MessageListenerImpl setSession(Session session) {
        this.session = session;
        return this;
    }

    public Object getBean() {
        return bean;
    }

    public MessageListenerImpl setBean(Object bean) {
        this.bean = bean;
        return this;
    }

    public Method getMethod() {
        return method;
    }

    public MessageListenerImpl setMethod(Method method) {
        this.method = method;
        return this;
    }

    public String getDestination() {
        return destination;
    }

    public MessageListenerImpl setDestination(String destination) {
        this.destination = destination;
        return this;
    }

    public Executor getExecutor() {
        return executor;
    }

    public MessageListenerImpl setExecutor(Executor executor) {
        this.executor = executor;
        return this;
    }

    public boolean isN2() {
        return n2;
    }

    public MessageListenerImpl setN2(boolean n2) {
        this.n2 = n2;
        return this;
    }

    public void invokeMethod(Event event) throws Exception {
        method.invoke(bean, event);
    }

    public void onMessage(final Message message) {
        final MapMessage mapMessage = (MapMessage) message;
        try {
            final String messageId = mapMessage.getString("messageId");
            executor.execute(new Runnable() {
                public void run() {
                    try {
                        if (messageId != null) {
                            final Map<String, Object> map = Maps.newHashMap();
                            map.put("data", mapMessage.getString("data"));
                            map.put("messageId", mapMessage.getString("messageId"));
                            map.put("timeStamp", mapMessage.getString("timeStamp"));
                            if (isN2()) {
                                map.put("topic", mapMessage.getString("topic"));
                                map.put("businessMark", mapMessage.getString("businessMark"));
                            }
                            repeatMessageHandler.handleRepeatMsg(map, MessageListenerImpl.this, isN2());
                            if (transaction) {
                                session.commit();
                            } else {
                                message.acknowledge();
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.error("handle message error:{}", e);
                    }
                }
            });

        } catch (Exception e) {
            LOGGER.error("handle message error: {}", e);
        }
    }
}

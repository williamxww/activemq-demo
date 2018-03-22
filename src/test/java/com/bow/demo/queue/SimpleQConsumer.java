package com.bow.demo.queue;

import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bow.demo.DemoMessageListener;

/**
 * @see SimpleQProducer
 * @author vv
 * @since 2017/4/2.
 */
public class SimpleQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleQConsumer.class);

    private static final String BROKER_URL = "tcp://localhost:61616";

    private ActiveMQConnectionFactory factory;

    private Connection connection;

    private Session session;

    public SimpleQConsumer(String brokerURL) throws JMSException {
        factory = new ActiveMQConnectionFactory(brokerURL);
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    /**
     * 创建消费者
     * 
     * @param queueName 队列的名称
     * @param listener 监听器
     * @throws JMSException
     */
    public void subscribe(String queueName, MessageListener listener) throws JMSException {
        Queue queue = session.createQueue(queueName);
        // 此处指定了messageSelector
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(listener);
    }

    public static void main(String[] args) throws Exception {
        SimpleQConsumer s1 = new SimpleQConsumer(BROKER_URL);
        String queueName = "q_vv";
        s1.subscribe(queueName, new DemoMessageListener("s1"));
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }
}

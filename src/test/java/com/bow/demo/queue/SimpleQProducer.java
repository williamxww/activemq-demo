package com.bow.demo.queue;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息放到队列中并且设置过期时间，过期后直接抛弃
 * 
 * <pre>
 <policyEntries>
 <policyEntry queue=">">
 <deadLetterStrategy>
 <!-- 过期消息直接丢掉不放到死信队列 -->
 <sharedDeadLetterStrategy processExpired="false" />
 </deadLetterStrategy>
 </policyEntry>
 </policyEntries>
 * </pre>
 *
 * @author vv
 * @since 2018/3/23.
 */
public class SimpleQProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleQProducer.class);

    private static final String BROKER_URL = "tcp://localhost:61616";

    private ActiveMQConnectionFactory factory;

    private Connection connection;

    private Session session;

    private MessageProducer producer;

    public SimpleQProducer(String brokerURL, String queueName) throws JMSException {
        factory = new ActiveMQConnectionFactory(brokerURL);
        connection = factory.createConnection();
        try {
            connection.start();
        } catch (JMSException e) {
            connection.close();
            throw e;
        }
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue(queueName);
        producer = session.createProducer(queue);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);
        // 设置消息过期时间
        producer.setDisableMessageTimestamp(false);
        producer.setTimeToLive(10_000);
    }

    public void send(String message) throws JMSException {
        TextMessage msg = session.createTextMessage(message);
        producer.send(msg);
    }

    public static void main(String[] args) throws Exception {
        SimpleQProducer qProducer = new SimpleQProducer(BROKER_URL, "q_vv");
        while (true) {
            qProducer.send("hello");
            System.out.println("Send 1 msg.");
            System.in.read();
        }
    }
}

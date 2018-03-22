package com.bow.demo.durable;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bow.demo.DemoMessageListener;

/**
 * producer发送持久化消息，但是consumer是普通的，因而离线后会丢消息。
 */
public class CommonSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonSubscriber.class);

    private ActiveMQConnectionFactory factory;

    private Connection connection;

    private Session session;

    public CommonSubscriber(String brokerURL) throws JMSException {
        factory = new ActiveMQConnectionFactory(brokerURL);
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public void subscribe(String topicName, MessageListener listener) throws JMSException {
        Topic topic = session.createTopic(topicName);
        // 此处指定了messageSelector,对消息进行过滤。
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(listener);
    }

    public static void main(String[] args) throws JMSException {
        String host = "tcp://localhost:61616";
        CommonSubscriber s1 = new CommonSubscriber(host);
        s1.subscribe("test-topic", new DemoMessageListener("s1"));
    }
}

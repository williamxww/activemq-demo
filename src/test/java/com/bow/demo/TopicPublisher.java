package com.bow.demo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import java.util.ArrayList;
import java.util.List;

/**
 * 发布-订阅模式,参照{@link TopicSubscriber}
 *
 * @author vv
 * @since 2017/4/1.
 */
public class TopicPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicPublisher.class);

    private ActiveMQConnectionFactory factory;

    private Connection connection;

    private Session session;

    private MessageProducer producer;

    private List<Topic> topics = new ArrayList();

    public TopicPublisher(String brokerURL) throws JMSException {
        factory = new ActiveMQConnectionFactory(brokerURL);
        connection = factory.createConnection();
        try {
            connection.start();
        } catch (JMSException e) {
            connection.close();
            throw e;
        }
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 不指定topic，producer就可以灵活的给任意topic发送信息了
        producer = session.createProducer(null);
    }

    public void createTopic(String topicName) throws JMSException {
        topics.add(session.createTopic(topicName));
    }

    public void sendMessage(String topicName, String group, String message) throws JMSException {
        for (Topic topic : topics) {
            if (topicName.equals(topic.getTopicName())) {
                TextMessage msg = session.createTextMessage(message);
                msg.setStringProperty("group", group);
                producer.send(topic, msg);
                LOGGER.info("send message " + message);
                return;
            }
        }
        LOGGER.error("can not send " + message + " on " + topicName);
    }

    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) throws Exception {
        TopicPublisher p1 = new TopicPublisher("tcp://localhost:61616");
        p1.createTopic("PRICE");
        while (true) {
            p1.sendMessage("PRICE", "group-a", "12");
            System.in.read();
        }

    }
}

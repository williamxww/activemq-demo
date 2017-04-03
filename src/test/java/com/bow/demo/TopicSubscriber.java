package com.bow.demo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;

/**
 * 基于topic和发布-订阅模式，发布者针对某topic发布消息，订阅者会收到通知(若指定了messageSelector，则会进行过滤)。 <br/>
 * {@link TopicPublisher}
 *
 *
 * 
 * @author vv
 * @since 2017/4/1.
 */
public class TopicSubscriber {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicSubscriber.class);

    private ActiveMQConnectionFactory factory;

    private Connection connection;

    private Session session;

    public TopicSubscriber(String brokerURL) throws JMSException {
        factory = new ActiveMQConnectionFactory(brokerURL);
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public void subscribe(String topicName, String group, MessageListener listener) throws JMSException {
        Topic topic = session.createTopic(topicName);
        // 此处指定了messageSelector,对消息进行过滤。
        MessageConsumer consumer = session.createConsumer(topic, "group='" + group + "'");
        consumer.setMessageListener(listener);
    }

    public static void main(String[] args) throws JMSException {
        String host = "tcp://localhost:61616";
        // 订阅者s1,s2会同时收到消息，s3和s1,s2不在同一个组所以收不到
        TopicSubscriber s1 = new TopicSubscriber(host);
        TopicSubscriber s2 = new TopicSubscriber(host);
        TopicSubscriber s3 = new TopicSubscriber(host);
        s1.subscribe("PRICE", "group-a", new DemoMessageListener("s1"));
        s2.subscribe("PRICE", "group-a", new DemoMessageListener("s2"));
        s2.subscribe("PRICE", "group-b", new DemoMessageListener("s3"));
    }
}

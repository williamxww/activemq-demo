package com.bow.demo;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * 参照{@link QueueConsumer}
 * @author vv
 * @since 2017/4/2.
 */
public class QueueProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueProducer.class);

    private ActiveMQConnectionFactory factory;

    private Connection connection;

    private Session session;

    private MessageProducer producer;

    private List<Queue> queues = new ArrayList();

    public QueueProducer(String brokerURL) throws JMSException {
        factory = new ActiveMQConnectionFactory(brokerURL);
        connection = factory.createConnection();
        try {
            connection.start();
        } catch (JMSException e) {
            connection.close();
            throw e;
        }
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //不指定topic，producer就可以灵活的给任意queue发送信息了
        producer = session.createProducer(null);
    }

    public void createQueue(String queueName) throws JMSException {
        queues.add(session.createQueue(queueName));
    }

    public void push(String queueName,String group, String message) throws JMSException {
        for (Queue queue : queues) {
            if (queueName.equals(queue.getQueueName())) {
                TextMessage msg = session.createTextMessage(message);
                msg.setStringProperty("group", group);
                producer.send(queue, msg);
                LOGGER.info("send message "+ message);
                return;
            }
        }
        LOGGER.error("can not push " + message + " to " + queueName);
    }

    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }

    public static void main(String[] args) throws Exception {
        QueueProducer p1 = new QueueProducer("tcp://localhost:61616");
        p1.createQueue("PRICE");
        while(true){
            p1.push("PRICE","group-a", "12");
            System.in.read();
        }

    }
}

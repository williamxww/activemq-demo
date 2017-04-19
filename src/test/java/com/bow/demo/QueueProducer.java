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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 参照{@link QueueConsumer}
 * 
 * @author vv
 * @since 2017/4/2.
 */
public class QueueProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueueProducer.class);

	private ActiveMQConnectionFactory factory;

	private Connection connection;

	private Session session;

	private MessageProducer producer;

	private Map<String, Queue> queues = new HashMap();

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
		// 不指定topic，producer就可以灵活的给任意queue发送信息了
		producer = session.createProducer(null);
	}

	public Queue createQueue(String queueName) throws JMSException {
		LOGGER.info("create queue " + queueName);
		Queue queue = session.createQueue(queueName);
		queues.put(queueName, queue);
		return queue;
	}

	public void send(String queueName, String group, String message) throws JMSException {
		Queue queue = queues.get(queueName);
		if (queue == null) {
			synchronized (QueueProducer.class) {
				queue = queues.get(queueName);
				if (queue == null) {
					queue = createQueue(queueName);
				}
			}
		}
		TextMessage msg = session.createTextMessage(message);
		msg.setStringProperty("group", group);
		producer.send(queue, msg);
	}

	public void close() throws JMSException {
		if (connection != null) {
			connection.close();
		}
	}

	public static void main(String[] args) throws Exception {
		QueueProducer p1 = new QueueProducer(
				"failover:(tcp://10.170.130.27:61616,tcp://10.170.130.27:61626,tcp://10.170.130.27:61636)");
		while (true) {
			System.in.read();
			p1.send("PRICE", "group-a", "12");
		}

	}
}

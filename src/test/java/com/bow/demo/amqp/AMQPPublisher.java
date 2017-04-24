package com.bow.demo.amqp;

import org.apache.qpid.jms.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class AMQPPublisher {
	private static final Logger LOGGER = LoggerFactory.getLogger(AMQPPublisher.class);
	private Session session;
	MessageProducer producer;

	public AMQPPublisher(String connectionURI, String queueName) {
		try {
			JmsConnectionFactory factory = new JmsConnectionFactory(connectionURI);
			Connection connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(queueName);
			producer = session.createProducer(destination);
			// producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void send(String message) throws JMSException {
		TextMessage msg = session.createTextMessage(message);
		producer.send(msg);
	}

}
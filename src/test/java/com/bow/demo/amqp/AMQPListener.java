package com.bow.demo.amqp;

import org.apache.qpid.jms.*;
import javax.jms.*;

public class AMQPListener {

	private MessageConsumer consumer;

	public AMQPListener(String connectionURI, String destinationName) {
		JmsConnectionFactory factory = new JmsConnectionFactory(connectionURI);
		try {
			Connection connection = factory.createConnection();
			connection.start();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(destinationName);
			consumer = session.createConsumer(destination);
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}

	public void subscribe(MessageListener listener) throws JMSException {
		consumer.setMessageListener(listener);
	}

}
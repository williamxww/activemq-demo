package com.bow.demo.queue;

import com.bow.demo.DemoMessageListener;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import java.util.concurrent.TimeUnit;

/**
 * 基于队列的生产者-消费者模式，生产者推送到队列中的消息，需要等待一个消费者取走。没有被取走的消息会一直呆在队列中。
 * 
 * @author vv
 * @since 2017/4/2.
 */
public class QueueConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueueConsumer.class);

	private ActiveMQConnectionFactory factory;

	private Connection connection;

	private Session session;

	public QueueConsumer(String brokerURL) throws JMSException {
		factory = new ActiveMQConnectionFactory(brokerURL);
		connection = factory.createConnection();
		connection.start();
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	}

	/**
	 * 创建消费者
	 * 
	 * @param queueName 队列的名称
	 * @param group 组名
	 * @param listener 监听器
	 * @throws JMSException
	 */
	public void subscribe(String queueName, String group, MessageListener listener) throws JMSException {
		Queue queue = session.createQueue(queueName);
		// 此处指定了messageSelector
		MessageConsumer consumer = session.createConsumer(queue, "group='" + group + "'");
		consumer.setMessageListener(listener);
	}

	public static void main(String[] args) throws Exception {
		// s1，s2轮流取走消息
		String host = "failover:(tcp://10.170.130.27:61616,tcp://10.170.130.27:61626,tcp://10.170.130.27:61636)";
		String optimizedParam = "?jms.optimizeAcknowledge=true&jms.optimizeAcknowledgeTimeOut=30000&jms.redeliveryPolicy.maximumRedeliveries=6";
		QueueConsumer s1 = new QueueConsumer(host+optimizedParam);
		String queueName = "PRICE?customer.prefetchSize=100";
		s1.subscribe(queueName, "group-a", new DemoMessageListener("s1"));
		TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
	}
}

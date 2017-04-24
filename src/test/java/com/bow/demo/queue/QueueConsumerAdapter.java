package com.bow.demo.queue;

import com.bow.benchmark.Consumer;
import com.bow.benchmark.IConsumer;
import com.bow.benchmark.StatsBenchmarkConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @author wwxiang
 * @since 2017/4/21.
 */
public class QueueConsumerAdapter implements IConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(QueueConsumerAdapter.class);

	private static String failoverHost = "failover:(tcp://10.170.130.27:61616,tcp://10.170.130.27:61626,tcp://10.170.130.27:61636)";
	private static String tcpHost = "tcp://127.0.0.1:61616";


	private static String optimizedParam = "?jms.optimizeAcknowledge=true&jms.optimizeAcknowledgeTimeOut=30000&jms.redeliveryPolicy.maximumRedeliveries=6";

	private static String topic = "PRICE?customer.prefetchSize=1000";
	private static String group = "group-a";

	private StatsBenchmarkConsumer statsBenchmarkConsumer;

	private QueueConsumer consumer;

	public QueueConsumerAdapter(StatsBenchmarkConsumer statsBenchmark) {
		try {
			this.statsBenchmarkConsumer = statsBenchmark;
			consumer = new QueueConsumer(tcpHost);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void receive() {
		try {
			consumer.subscribe(topic, group, new MessageListener() {
				@Override
				public void onMessage(Message message) {
					try {
						long now = System.currentTimeMillis();
						statsBenchmarkConsumer.getReceiveMessageTotalCount().incrementAndGet();
						long born2ConsumerRT = now - message.getJMSTimestamp();
						statsBenchmarkConsumer.getBorn2ConsumerTotalRT().addAndGet(born2ConsumerRT);
						long store2ConsumerRT = 0;
						// statsBenchmarkConsumer.getStore2ConsumerTotalRT().addAndGet(store2ConsumerRT);
						Consumer.compareAndSetMax(statsBenchmarkConsumer.getBorn2ConsumerMaxRT(), born2ConsumerRT);
						Consumer.compareAndSetMax(statsBenchmarkConsumer.getStore2ConsumerMaxRT(), store2ConsumerRT);
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}

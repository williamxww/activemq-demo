package com.bow.demo.amqp;

import com.bow.benchmark.Consumer;
import com.bow.benchmark.IConsumer;
import com.bow.benchmark.StatsBenchmarkConsumer;
import com.bow.demo.queue.QueueConsumer;
import com.bow.mq.MessageListenerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * @author wwxiang
 * @since 2017/4/21.
 */
public class AMQPListenerAdapter implements IConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AMQPListenerAdapter.class);

	private static String amqpHost = "amqp://10.170.130.27:5673";
	private static String topic = "PRICE?customer.prefetchSize=1000";

	private AMQPListener consumer;

	private StatsBenchmarkConsumer statsBenchmarkConsumer;

	public AMQPListenerAdapter(StatsBenchmarkConsumer statsBenchmark) {
		this.statsBenchmarkConsumer = statsBenchmark;
		consumer = new AMQPListener(amqpHost, topic);
	}

	@Override
	public void receive() {
		try {
			consumer.subscribe(new MessageListener() {
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

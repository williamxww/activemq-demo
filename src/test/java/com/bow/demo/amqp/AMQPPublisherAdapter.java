package com.bow.demo.amqp;

import com.bow.benchmark.IProducer;
import com.bow.benchmark.Producer;
import com.bow.benchmark.StatsBenchmarkProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;

/**
 * @author wwxiang
 * @since 2017/4/21.
 */
public class AMQPPublisherAdapter implements IProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(AMQPPublisherAdapter.class);

	private static String topic = "PRICE";
	private static String amqpHost = "amqp://10.170.130.27:5673";

	private StatsBenchmarkProducer statsBenchmark;

	private AMQPPublisher publisher;

	public AMQPPublisherAdapter(StatsBenchmarkProducer statsBenchmark) {
		this.statsBenchmark = statsBenchmark;

		publisher = new AMQPPublisher(amqpHost, topic);
	}

	@Override
	public void send(String message) {
		try {
			final long beginTimestamp = System.currentTimeMillis();
			publisher.send(message);
			statsBenchmark.getSendRequestSuccessCount().incrementAndGet();
			statsBenchmark.getReceiveResponseSuccessCount().incrementAndGet();
			final long currentRT = System.currentTimeMillis() - beginTimestamp;
			statsBenchmark.getSendMessageSuccessTimeTotal().addAndGet(currentRT);
			Producer.compareAndSetMax(statsBenchmark.getSendMessageMaxRT(), currentRT);
		} catch (JMSException e) {
			statsBenchmark.getSendRequestFailedCount().incrementAndGet();
			// statsBenchmark.getReceiveResponseFailedCount().incrementAndGet();
			LOGGER.error("[BENCHMARK_PRODUCER] Send Exception", e);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException ignored) {
			}
		}
	}
}

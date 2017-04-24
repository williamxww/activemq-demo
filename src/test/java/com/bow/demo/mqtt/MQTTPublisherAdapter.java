package com.bow.demo.mqtt;

import com.bow.benchmark.IProducer;
import com.bow.benchmark.Producer;
import com.bow.benchmark.StatsBenchmarkProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wwxiang
 * @since 2017/4/21.
 */
public class MQTTPublisherAdapter implements IProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

	private static String host = "10.170.130.27";
	private static int port = 1883;
	private static String destination = "PRICE";

	private StatsBenchmarkProducer statsBenchmark;

	private MQTTPublisher publisher;

	public MQTTPublisherAdapter(StatsBenchmarkProducer statsBenchmark) {
		this.statsBenchmark = statsBenchmark;
		publisher = new MQTTPublisher(host, port);
	}

	@Override
	public void send(String message) {
		try {
			final long beginTimestamp = System.currentTimeMillis();
			publisher.send(destination, message);
			statsBenchmark.getSendRequestSuccessCount().incrementAndGet();
			statsBenchmark.getReceiveResponseSuccessCount().incrementAndGet();
			final long currentRT = System.currentTimeMillis() - beginTimestamp;
			statsBenchmark.getSendMessageSuccessTimeTotal().addAndGet(currentRT);
			Producer.compareAndSetMax(statsBenchmark.getSendMessageMaxRT(), currentRT);
		} catch (Exception e) {
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

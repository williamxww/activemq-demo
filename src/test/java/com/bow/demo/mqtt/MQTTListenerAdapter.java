package com.bow.demo.mqtt;

import com.bow.benchmark.Consumer;
import com.bow.benchmark.IConsumer;
import com.bow.benchmark.StatsBenchmarkConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wwxiang
 * @since 2017/4/21.
 */
public class MQTTListenerAdapter implements IConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MQTTListenerAdapter.class);

	private static String host = "10.170.130.27";
	private static int port = 1883;
	private static String destination = "PRICE";

	private StatsBenchmarkConsumer statsBenchmarkConsumer;

	private MQTTListener listener;

	public MQTTListenerAdapter(StatsBenchmarkConsumer benchmarkConsumer) {
		this.statsBenchmarkConsumer = benchmarkConsumer;
		listener = new MQTTListener(host, port);
	}

	@Override
	public void receive() {
		listener.subscribe(destination, new TextMessageListener() {
			@Override
			public void onMessage(String message) {
				long now = System.currentTimeMillis();
				statsBenchmarkConsumer.getReceiveMessageTotalCount().incrementAndGet();
				long born2ConsumerRT = 0;
				statsBenchmarkConsumer.getBorn2ConsumerTotalRT().addAndGet(born2ConsumerRT);
				long store2ConsumerRT = 0;
				// statsBenchmarkConsumer.getStore2ConsumerTotalRT().addAndGet(store2ConsumerRT);
				Consumer.compareAndSetMax(statsBenchmarkConsumer.getBorn2ConsumerMaxRT(), born2ConsumerRT);
				Consumer.compareAndSetMax(statsBenchmarkConsumer.getStore2ConsumerMaxRT(), store2ConsumerRT);
			}
		});
	}
}

package com.bow.benchmark;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wwxiang
 * @since 2017/4/21.
 */
public class StatsBenchmarkConsumer {
	/**
	 * 总接收数
	 */
	private final AtomicLong receiveMessageTotalCount = new AtomicLong(0L);

	/**
	 * 消息传递总时间
	 */
	private final AtomicLong born2ConsumerTotalRT = new AtomicLong(0L);

	/**
	 * 从MQ到消费者时间-暂不测
	 */
	private final AtomicLong store2ConsumerTotalRT = new AtomicLong(0L);

	private final AtomicLong born2ConsumerMaxRT = new AtomicLong(0L);

	private final AtomicLong store2ConsumerMaxRT = new AtomicLong(0L);

	public Long[] createSnapshot() {

		Long[] snap = new Long[] { System.currentTimeMillis(), this.receiveMessageTotalCount.get(),
				this.born2ConsumerTotalRT.get(), this.store2ConsumerTotalRT.get(), this.born2ConsumerMaxRT.get(),
				this.store2ConsumerMaxRT.get() };

		// 最大只是代表这段时间内的最大,在下一段时间内重新比较最大
		this.born2ConsumerMaxRT.set(0);
		this.store2ConsumerMaxRT.set(0);
		return snap;
	}

	public AtomicLong getReceiveMessageTotalCount() {
		return receiveMessageTotalCount;
	}

	public AtomicLong getBorn2ConsumerTotalRT() {
		return born2ConsumerTotalRT;
	}

	public AtomicLong getStore2ConsumerTotalRT() {
		return store2ConsumerTotalRT;
	}

	public AtomicLong getBorn2ConsumerMaxRT() {
		return born2ConsumerMaxRT;
	}

	public AtomicLong getStore2ConsumerMaxRT() {
		return store2ConsumerMaxRT;
	}
}
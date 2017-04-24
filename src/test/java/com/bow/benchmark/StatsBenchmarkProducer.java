package com.bow.benchmark;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author wwxiang
 * @since 2017/4/21.
 */
public class StatsBenchmarkProducer {
    private final AtomicLong sendRequestSuccessCount = new AtomicLong(0L);

    private final AtomicLong sendRequestFailedCount = new AtomicLong(0L);

    private final AtomicLong receiveResponseSuccessCount = new AtomicLong(0L);

    private final AtomicLong receiveResponseFailedCount = new AtomicLong(0L);

    private final AtomicLong sendMessageSuccessTimeTotal = new AtomicLong(0L);

    private final AtomicLong sendMessageMaxRT = new AtomicLong(0L);

    public Long[] createSnapshot() {
        Long[] snap = new Long[] { System.currentTimeMillis(), this.sendRequestSuccessCount.get(),
                this.sendRequestFailedCount.get(), this.receiveResponseSuccessCount.get(),
                this.receiveResponseFailedCount.get(), this.sendMessageSuccessTimeTotal.get(), sendMessageMaxRT.get() };
        // 重新统计下个阶段的最大时间
        this.sendMessageMaxRT.set(0);
        return snap;
    }

    public AtomicLong getSendRequestSuccessCount() {
        return sendRequestSuccessCount;
    }

    public AtomicLong getSendRequestFailedCount() {
        return sendRequestFailedCount;
    }

    public AtomicLong getReceiveResponseSuccessCount() {
        return receiveResponseSuccessCount;
    }

    public AtomicLong getReceiveResponseFailedCount() {
        return receiveResponseFailedCount;
    }

    public AtomicLong getSendMessageSuccessTimeTotal() {
        return sendMessageSuccessTimeTotal;
    }

    public AtomicLong getSendMessageMaxRT() {
        return sendMessageMaxRT;
    }
}

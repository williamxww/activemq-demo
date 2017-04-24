package com.bow.benchmark;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

import com.bow.demo.amqp.AMQPPublisherAdapter;
import com.bow.demo.mqtt.MQTTPublisherAdapter;
import com.bow.demo.queue.QueueProducerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Producer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    private static final int FRQ_SNAPSHOT = 1000;

    private static final int FRQ_PRINT = 5000;

    public void startTimer() {
        final int threadCount = 1;
        final int messageSize = 1000;
        final int numPerThread = 1_0000;
        LOGGER.info("ThreadCount {} messageSize {} ", threadCount, messageSize);
        final String msg = buildMessage(messageSize);
        final ExecutorService sendThreadPool = Executors.newFixedThreadPool(threadCount);
        final StatsBenchmarkProducer statsBenchmark = new StatsBenchmarkProducer();
        final Timer timer = new Timer("BenchmarkTimerThread", true);
        final LinkedList<Long[]> snapshotList = new LinkedList();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                snapshotList.addLast(statsBenchmark.createSnapshot());
                if (snapshotList.size() > 10) {
                    snapshotList.removeFirst();
                }
            }
        }, FRQ_SNAPSHOT, FRQ_SNAPSHOT);

        timer.scheduleAtFixedRate(new TimerTask() {
            private void printStats() {
                if (snapshotList.size() >= 10) {
                    Long[] begin = snapshotList.getFirst();
                    Long[] end = snapshotList.getLast();

                    final long sendTps = (long) (((end[3] - begin[3]) / (double) (end[0] - begin[0])) * 1000L);
                    final double averageRT = (end[5] - begin[5]) / (double) (end[3] - begin[3]);

                    System.out.printf(
                            "Send TPS:%d,  Max Send Time:%d,  Average Send Time:%7.3f,  Send Num:%d,  Send Failed:%d%n",
                            sendTps, end[6], averageRT, end[1], end[2]);
                }
            }

            @Override
            public void run() {
                try {
                    this.printStats();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, FRQ_PRINT, FRQ_PRINT);

        // 待测试项
        IProducer producer = new QueueProducerAdapter(statsBenchmark);
        // // IProducer producer = new AMQPPublisherAdapter(statsBenchmark);
        // IProducer producer = new MQTTPublisherAdapter(statsBenchmark);

        for (int i = 0; i < threadCount; i++) {
            sendThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < numPerThread; i++) {
                        // 发送消息
                        producer.send(msg);
                    }
                }
            });
        }
    }

    public static void compareAndSetMax(final AtomicLong target, final long value) {
        long prev = target.get();
        while (value > prev) {
            boolean updated = target.compareAndSet(prev, value);
            if (updated) {
                break;
            }
            prev = target.get();
        }
    }

    private static String buildMessage(final int messageSize) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < messageSize; i += 10) {
            sb.append("hello baby");
        }
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        Producer producer = new Producer();
        producer.startTimer();

    }
}

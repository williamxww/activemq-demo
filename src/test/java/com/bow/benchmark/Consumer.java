
package com.bow.benchmark;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import com.bow.demo.amqp.AMQPListenerAdapter;
import com.bow.demo.mqtt.MQTTListenerAdapter;
import com.bow.demo.queue.QueueConsumerAdapter;

public class Consumer {

    private static final int FRQ_SNAPSHOT = 1000;

    private static final int FRQ_PRINT = 5000;

    private void startTimer() {
        final StatsBenchmarkConsumer statsBenchmarkConsumer = new StatsBenchmarkConsumer();
        final Timer timer = new Timer("BenchmarkTimerThread", true);
        final LinkedList<Long[]> snapshotList = new LinkedList();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                snapshotList.addLast(statsBenchmarkConsumer.createSnapshot());
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
                    // 0时间戳，1总接收数，2总born-consume消耗时间，3总store2Consume时间
                    // 4born-consume最大消耗时间，5 store2Consume最大时间
                    final long consumeTps = (long) (((end[1] - begin[1]) / (double) (end[0] - begin[0])) * 1000L);
                    final double averageB2CRT = (end[2] - begin[2]) / (double) (end[1] - begin[1]);
                    // final double averageS2CRT = (end[3] - begin[3]) /
                    // (double) (end[1] - begin[1]);
                    System.out.printf(
                            "Consume TPS:%d,  Average Time(B2C):%7.3f,  MAX Time(B2C):%d,  Consume Total:%d%n",
                            consumeTps, averageB2CRT, end[4], end[1]);
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

        // 开启接收消息
        IConsumer consumer = new QueueConsumerAdapter(statsBenchmarkConsumer);
        // // IConsumer consumer = new
        // AMQPListenerAdapter(statsBenchmarkConsumer);
        // IConsumer consumer = new MQTTListenerAdapter(statsBenchmarkConsumer);
        consumer.receive();
        System.out.printf("Consumer Started.%n");
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

    public static void main(String[] args) throws Exception {
        Consumer consumer = new Consumer();
        consumer.startTimer();
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }
}

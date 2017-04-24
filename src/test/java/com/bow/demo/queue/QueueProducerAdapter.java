package com.bow.demo.queue;

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
public class QueueProducerAdapter implements IProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    private static String topic = "PRICE";

    private static String group = "group-a";

    private static String failoverHost = "failover:(tcp://10.170.130.27:61616,tcp://10.170.130.27:61626,tcp://10.170.130.27:61636)";

    private static String tcpHost = "tcp://127.0.0.1:61616";

    private static String mqttHost = "mqtt://10.170.130.27:1883";

    private StatsBenchmarkProducer statsBenchmark;

    private QueueProducer producer;

    public QueueProducerAdapter(StatsBenchmarkProducer statsBenchmark) {
        try {
            this.statsBenchmark = statsBenchmark;
            producer = new QueueProducer(tcpHost);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String message) {
        try {
            final long beginTimestamp = System.currentTimeMillis();
            producer.send(topic, group, message);
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

package com.bow.demo.mqtt;

import org.fusesource.hawtbuf.AsciiBuffer;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;

/**
 * Uses a Future based API to MQTT.
 */
public class MQTTPublisher {

	private static final Logger LOGGER = LoggerFactory.getLogger(MQTTPublisher.class);

	private MQTT mqtt;
	private FutureConnection connection;
	private LinkedList<Future<Void>> queue = new LinkedList<Future<Void>>();

	public MQTTPublisher(String host, int port) {
		try {
			mqtt = new MQTT();
			mqtt.setHost(host, port);
			connection = mqtt.futureConnection();
			connection.connect().await();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void send(String destination, String message) throws Exception {
		// Send the publish without waiting for it to complete. This allows
		// us
		// to send multiple message without blocking..
		UTF8Buffer topic = new UTF8Buffer(destination);
		Buffer msg = new AsciiBuffer(message);
		queue.add(connection.publish(topic, msg, QoS.EXACTLY_ONCE, false));

		// Eventually we start waiting for old publish futures to complete
		// so that we don't create a large in memory buffer of outgoing
		// message.s
		if (queue.size() >= 1000) {
			queue.removeFirst().await();
		}
	}

}
/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bow.demo.mqtt;

import com.bow.demo.queue.QueueConsumerAdapter;
import org.fusesource.hawtbuf.*;
import org.fusesource.mqtt.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.MessageListener;
import java.net.URISyntaxException;

/**
 * Uses an callback based interface to MQTT. Callback based interfaces are
 * harder to use but are slightly more efficient.
 */
public class MQTTListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(MQTTListener.class);

	private MQTT mqtt;

	public MQTTListener(String host, int port) {
		try {
			mqtt = new MQTT();
			mqtt.setHost(host, port);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void subscribe(String destination, TextMessageListener listener) {
		CallbackConnection connection = mqtt.callbackConnection();
		connection.listener(new org.fusesource.mqtt.client.Listener() {
			public void onConnected() {
			}

			public void onDisconnected() {
			}

			public void onFailure(Throwable value) {
				LOGGER.error("", value);
			}

			public void onPublish(UTF8Buffer topic, Buffer msg, Runnable ack) {
				String body = msg.utf8().toString();
				listener.onMessage(body);
				ack.run();
			}
		});
		connection.connect(new Callback<Void>() {
			@Override
			public void onSuccess(Void value) {
				Topic[] topics = { new Topic(destination, QoS.AT_LEAST_ONCE) };
				connection.subscribe(topics, new Callback<byte[]>() {
					@Override
					public void onSuccess(byte[] bytes) {
						LOGGER.info(new String(bytes));
					}

					@Override
					public void onFailure(Throwable throwable) {
						LOGGER.error("", value);
					}
				});
			}

			@Override
			public void onFailure(Throwable value) {
				LOGGER.error("", value);
			}
		});
	}

}
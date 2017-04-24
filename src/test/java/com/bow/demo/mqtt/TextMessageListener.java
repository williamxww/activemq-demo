package com.bow.demo.mqtt;

/**
 * @author wwxiang
 * @since 2017/4/21.
 */
public interface TextMessageListener {
	void onMessage(String message);
}

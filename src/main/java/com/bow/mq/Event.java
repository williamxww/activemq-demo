package com.bow.mq;

public interface Event<T> {

    /**
     * 消息Id
     * 
     * @return
     */
    String messageId();

    /**
     * 消息地址
     * 
     * @return
     */
    String topic();

    /**
     * 消息内容
     * 
     * @return
     */
    T content();
}

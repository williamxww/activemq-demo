package com.bow.mq;

public interface MessageCallback {
    void onSuccess(String messageId);

    void onFail(Exception e, String messageId);
}

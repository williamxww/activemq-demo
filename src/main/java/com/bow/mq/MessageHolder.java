package com.bow.mq;

import java.util.List;

import com.google.common.collect.Lists;

public class MessageHolder {

    /**
     * 每个线程有ThreadLocalMap，里面存放了key=messageHolder value=Object的一组值，所以messageHolder能够取出值
     */
    private static ThreadLocal<List<String>> messageHolder = new ThreadLocal<List<String>>() {

        @Override
        protected List<String> initialValue() {
            return Lists.newArrayList();
        }
    };

    public static List<String> get() {
        return messageHolder.get();
    }

    public static void set(String messageId) {
        List<String> list = messageHolder.get();
        list.add(messageId);
        messageHolder.set(list);
    }

    public static void clear() {
        messageHolder.remove();
    }
}

package com.bow.demo.embed;

import org.apache.activemq.xbean.BrokerFactoryBean;

/**
 * 内嵌的MQ启动失败不能影响应用。
 * @author vv
 * @since 2017/12/10.
 */
public class EmbedBrokerFactoryBean extends BrokerFactoryBean {

    @Override
    public void afterPropertiesSet() throws Exception {
        try {
            super.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

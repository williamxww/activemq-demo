package com.bow.entity;

import java.io.Serializable;

public class N2Record implements Serializable {

    private Integer id;

    /**
     * 业务线标识
     */
    private String businessMark;

    private Long timeStamp;

    /**
     * 队列名称
     */
    private String destName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBusinessMark() {
        return businessMark;
    }

    public void setBusinessMark(String businessMark) {
        this.businessMark = businessMark;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }
}

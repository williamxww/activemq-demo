package com.bow.service;

import com.bow.entity.N1Record;

import java.util.Date;

public interface N1RecordService {

    /**
     * 获取N1Record
     *
     * @param messageId
     * @return
     */
    N1Record selectN1Record(String messageId);

    /**
     * 添加N1Record
     *
     * @param n1Record
     * @return
     */
    int addN1Record(N1Record n1Record);

    /**
     * 删除N1Record
     *
     * @param timeStamp
     * @return
     */
    int deleteN1Record(Date timeStamp);
}

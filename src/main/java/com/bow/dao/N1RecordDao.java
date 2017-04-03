package com.bow.dao;

import com.bow.entity.N1Record;

import java.util.Date;

public interface N1RecordDao {

    N1Record selectN1Record(String messageId);

    int addN1Record(N1Record n1Record);

    int deleteN1Record(Date timeStamp);
}

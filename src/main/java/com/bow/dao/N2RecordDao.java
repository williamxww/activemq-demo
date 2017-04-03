package com.bow.dao;

import com.bow.entity.N2Record;

import java.util.Map;

public interface N2RecordDao {

    N2Record getN2RecordByMark(Map<String, Object> params);

    N2Record getN2Record(Map<String, Object> params);

    int addN2Record(N2Record n2Record);

    int updateN2Record(N2Record n2Record);
}

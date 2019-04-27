package com.sun.health.newwork.record.service;

import com.sun.health.newwork.record.dto.QueryCatalogDTO;
import com.sun.health.newwork.record.dto.QueryRecordDTO;
import com.sun.health.newwork.record.entity.Record;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RecordService {

    Record saveRecord(Record record);

    Record updateRecord(Record record);

    int deleteRecord(Record record);

    List<Record> searchSelfRecords();

    List<Record> searchAllRecords();

    Record queryRecordDetail(Long recordId);

    Page<Record> queryRecordPage(QueryRecordDTO queryRecordDTO);

    List<Record> queryRecordList(QueryRecordDTO queryRecordDTO);

}

package com.sun.health.newwork.record.service.impl;

import com.sun.health.newwork.record.dto.QueryRecordDTO;
import com.sun.health.newwork.record.entity.Record;
import com.sun.health.newwork.record.repository.RecordRepository;
import com.sun.health.newwork.record.service.RecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Record saveRecord(Record record) {
        record.setTagSet(record.getTagArr());
        return recordRepository.save(record);
    }

    @Override
    public Record updateRecord(Record record) {
        return recordRepository.save(record);
    }

    @Override
    public int deleteRecord(Record record) {
        recordRepository.delete(record);
        return 0;
    }

    @Override
    public List<Record> searchSelfRecords() {
        return null;
    }

    @Override
    public List<Record> searchAllRecords() {
        return null;
    }

    @Override
    public Record queryRecordDetail(Long recordId) {
        Record record = recordRepository.findById(recordId).get();
        record.getTagSet().size();
        return record;
    }

    @Override
    public Page<Record> queryRecordPage(QueryRecordDTO queryRecordDTO) {
        StringBuilder fromSql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        fromSql.append(" FROM Record AS r LEFT JOIN r.catalog LEFT JOIN r.catalog.createUser LEFT JOIN r.publishUser WHERE 1=1");
        if(queryRecordDTO.getId() != null) {
            fromSql.append(" AND r.id=:id");
            params.put("id", queryRecordDTO.getId());
        }
        if(StringUtils.isNotBlank(queryRecordDTO.getTitle())) {
            fromSql.append(" AND r.title LIKE :title");
            params.put("title", "%" + queryRecordDTO.getTitle() + "%");
        }
        if(StringUtils.isNotBlank(queryRecordDTO.getContent())) {
            fromSql.append(" AND r.content LIKE :content");
            params.put("content", "%" + queryRecordDTO.getContent() + "%");
        }
        if(queryRecordDTO.getPv() != null) {
            fromSql.append(" AND r.pv = :pv");
            params.put("pv", queryRecordDTO.getPv());
        }
        if(queryRecordDTO.getUpdateTimeBefore() != null) {
            fromSql.append(" AND r.updateTime >= :updateTimeBefore");
            params.put("updateTimeBefore", queryRecordDTO.getUpdateTimeBefore());
        }
        if(queryRecordDTO.getUpdateTimeAfter() != null) {
            fromSql.append(" AND r.updateTime <= :updateTimeAfter");
            params.put("updateTimeAfter", queryRecordDTO.getUpdateTimeAfter());
        }
        if(queryRecordDTO.getPublishUserId() != null) {
            fromSql.append(" AND r.publishUser.id = :publishUserId");
            params.put("publishUserId", queryRecordDTO.getPublishUserId());
        }
        if(queryRecordDTO.getCatalogId() != null) {
            fromSql.append(" AND r.catalog.id = :catalogId");
            params.put("catalogId", queryRecordDTO.getCatalogId());
        }
        String selectSql = "SELECT new Record(r.id, r.title, r.content, r.updateTime, r.lastModifyTime, r.pv, r.catalog.id, r.catalog.name, r.catalog.createUser.id, r.catalog.createUser.username, r.publishUser.id, r.publishUser.username)";
        String countSql = "SELECT COUNT(1)";
        TypedQuery<Record> query = entityManager.createQuery(selectSql + fromSql.toString() + " ORDER BY r.updateTime DESC", Record.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countSql + fromSql.toString(), Long.class);
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            query.setParameter(stringObjectEntry.getKey(), stringObjectEntry.getValue());
            countQuery.setParameter(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        Pageable pageable = queryRecordDTO.getPageable();
        if(pageable != null) {
            int pageNumber = pageable.getPageNumber();
            int pageSize = pageable.getPageSize();
            query.setFirstResult(pageNumber * pageSize);
            query.setMaxResults(pageSize);
        }
        return new PageImpl<Record>(query.getResultList(), pageable, countQuery.getSingleResult());
    }

    @Override
    public List<Record> queryRecordList(QueryRecordDTO queryRecordDTO) {
        StringBuilder fromSql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        fromSql.append(" LEFT JOIN r.catalog LEFT JOIN r.catalog.createUser LEFT JOIN r.publishUser FROM Record AS r WHERE 1=1");
        if(queryRecordDTO.getId() != null) {
            fromSql.append(" AND r.id=:id");
            params.put("id", queryRecordDTO.getId());
        }
        if(StringUtils.isNotBlank(queryRecordDTO.getTitle())) {
            fromSql.append(" AND r.title LIKE :title");
            params.put("title", "%" + queryRecordDTO.getTitle() + "%");
        }
        if(StringUtils.isNotBlank(queryRecordDTO.getContent())) {
            fromSql.append(" AND r.content LIKE :content");
            params.put("content", "%" + queryRecordDTO.getContent() + "%");
        }
        if(queryRecordDTO.getPv() != null) {
            fromSql.append(" AND r.pv = :pv");
            params.put("pv", queryRecordDTO.getPv());
        }
        if(queryRecordDTO.getUpdateTimeBefore() != null) {
            fromSql.append(" AND r.updateTime >= :updateTimeBefore");
            params.put("updateTimeBefore", queryRecordDTO.getUpdateTimeBefore());
        }
        if(queryRecordDTO.getUpdateTimeAfter() != null) {
            fromSql.append(" AND r.updateTime <= :updateTimeAfter");
            params.put("updateTimeAfter", queryRecordDTO.getUpdateTimeAfter());
        }
        if(queryRecordDTO.getPublishUserId() != null) {
            fromSql.append(" AND r.publishUser.id = :publishUserId");
            params.put("publishUserId", queryRecordDTO.getPublishUserId());
        }
        if(queryRecordDTO.getCatalogId() != null) {
            fromSql.append(" AND r.catalog.id = :catalogId");
            params.put("catalogId", queryRecordDTO.getCatalogId());
        }
        String selectSql = "SELECT new Record(r.id, r.title, r.content, r.updateTime, r.lastModifyTime, r.pv, r.catalog.id, r.catalog.name, r.catalog.createUser.id, r.catalog.createUser.username, r.publishUser.id, r.publishUser.username)";
        TypedQuery<Record> query = entityManager.createQuery(selectSql + fromSql.toString() + " ORDER BY r.createTime DESC", Record.class);
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            query.setParameter(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        Pageable pageable = queryRecordDTO.getPageable();
        if(pageable != null) {
            int pageNumber = pageable.getPageNumber();
            int pageSize = pageable.getPageSize();
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
        }
        return query.getResultList();
    }

}

package com.sun.health.newwork.record.service.impl;

import com.sun.health.newwork.record.entity.RecordTag;
import com.sun.health.newwork.record.repository.RecordTagRepository;
import com.sun.health.newwork.record.service.RecordTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RecordTagServiceImpl implements RecordTagService {

    @Autowired
    private RecordTagRepository recordTagRepository;

    @Override
    public RecordTag save(RecordTag recordTag) {
        return recordTagRepository.save(recordTag);
    }

    @Override
    public int deleteByRecordIdAndTagId(Long recordId, Long tagId) {
        return recordTagRepository.deleteByRecordIdAndTagId(recordId, tagId);
    }
}

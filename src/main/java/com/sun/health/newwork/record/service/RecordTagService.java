package com.sun.health.newwork.record.service;

import com.sun.health.newwork.record.entity.RecordTag;

public interface RecordTagService {

    RecordTag save(RecordTag recordTag);

    int deleteByRecordIdAndTagId(Long recordId, Long tagId);

}

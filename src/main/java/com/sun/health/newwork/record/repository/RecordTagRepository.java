package com.sun.health.newwork.record.repository;

import com.sun.health.newwork.record.entity.RecordTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RecordTagRepository extends JpaRepository<RecordTag, Long> {

//    @Modifying
//    @Query("DELETE FROM RecordTag WHERE recordId = ?1 AND tagId = ?2")
    int deleteByRecordIdAndTagId(Long recordId, Long tagId);

}

package com.sun.health.newwork.record.repository;

import com.sun.health.newwork.record.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

    @Query(nativeQuery = true, value = "SELECT t.* FROM tag AS t, record_tag AS rt WHERE rt.record_id = ?1 AND rt.tag_id = t.id")
    List<Tag> findByRecordId(Long recordId);

    Tag findByName(String name);

}

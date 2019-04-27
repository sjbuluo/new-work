package com.sun.health.newwork.record.repository;

import com.sun.health.newwork.record.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Page<Reply> findByReplyRecordId(Long recordId, Pageable pageable);

}

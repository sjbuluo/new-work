package com.sun.health.newwork.record.service;

import com.sun.health.newwork.record.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.nio.file.attribute.UserPrincipalNotFoundException;

public interface ReplyService {

    Reply save(Reply reply) throws UserPrincipalNotFoundException;

    Page<Reply> queryRecordReplyPage(Long recordId, Pageable pageable);

}

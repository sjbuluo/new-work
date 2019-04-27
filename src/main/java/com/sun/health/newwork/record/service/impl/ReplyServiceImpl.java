package com.sun.health.newwork.record.service.impl;

import com.sun.health.newwork.base.util.UserUtils;
import com.sun.health.newwork.record.entity.Reply;
import com.sun.health.newwork.record.repository.ReplyRepository;
import com.sun.health.newwork.record.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Date;

@Service
@Transactional
public class ReplyServiceImpl implements ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    @Override
    public Reply save(Reply reply) throws UserPrincipalNotFoundException {
        if(reply.getReplyTime() == null) {
            reply.setReplyTime(new Date());
        }
        if(reply.getReplyUser() == null) {
            reply.setReplyUser(UserUtils.getSignInUser());
        }
        reply.setReplyRecord(reply.getTransientReplyRecord());
        return replyRepository.save(reply);
    }

    @Override
    public Page<Reply> queryRecordReplyPage(Long recordId, Pageable pageable) {
        return replyRepository.findByReplyRecordId(recordId, pageable);
    }
}

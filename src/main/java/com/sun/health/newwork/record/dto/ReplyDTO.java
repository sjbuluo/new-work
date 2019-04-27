package com.sun.health.newwork.record.dto;

import com.sun.health.newwork.base.dto.UserDTO;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

public class ReplyDTO implements Serializable {

    private static final long serialVersionUID = 7911253461497094511L;

    private Long id;

    private Long userId;

    private String content;

    private Date replyTime;

    private Date lastModifyTime;

    private Long quoteReplyId;

    private UserDTO replyUser;

    private ReplyDTO quoteReply;

    public ReplyDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Long getQuoteReplyId() {
        return quoteReplyId;
    }

    public void setQuoteReplyId(Long quoteReplyId) {
        this.quoteReplyId = quoteReplyId;
    }

    public UserDTO getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(UserDTO replyUser) {
        this.replyUser = replyUser;
    }

    public ReplyDTO getQuoteReply() {
        return quoteReply;
    }

    public void setQuoteReply(ReplyDTO quoteReply) {
        this.quoteReply = quoteReply;
    }
}

package com.sun.health.newwork.record.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.health.newwork.base.entity.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "reply")
public class Reply {

    private Long id;

    private String content;

    private Date replyTime;

    private Date lastModifyTime;

    private Reply quoteReply;

    @JsonIgnore
    private Set<Reply> replySet;

    @JsonIgnore
    private Record replyRecord;

    private Record transientReplyRecord;

    private User replyUser;

    public Reply() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "reply_time")
    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    @Column(name = "last_modify_time")
    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quote_reply_id")
    public Reply getQuoteReply() {
        return quoteReply;
    }

    public void setQuoteReply(Reply quoteReply) {
        this.quoteReply = quoteReply;
    }

    @OneToMany(mappedBy = "quoteReply", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    public Set<Reply> getReplySet() {
        return replySet;
    }

    public void setReplySet(Set<Reply> replySet) {
        this.replySet = replySet;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    public Record getReplyRecord() {
        return replyRecord;
    }

    public void setReplyRecord(Record replyRecord) {
        this.replyRecord = replyRecord;
    }

    @Transient
    public Record getTransientReplyRecord() {
        return transientReplyRecord;
    }

    public void setTransientReplyRecord(Record transientReplyRecord) {
        this.transientReplyRecord = transientReplyRecord;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public User getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(User replyUser) {
        this.replyUser = replyUser;
    }
}

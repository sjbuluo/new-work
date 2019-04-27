package com.sun.health.newwork.record.dto;

import com.sun.health.newwork.record.entity.Reply;
import com.sun.health.newwork.record.entity.Tag;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class QueryRecordDTO implements Serializable {

    private static final long serialVersionUID = 4469191917481686747L;
    private Long id;
    private String title;
    private String content;
    private Date updateTimeBefore;
    private Date updateTimeAfter;
    private Boolean pv;
    private Long publishUserId;
    private Long catalogId;
    private List<Tag> tags;
    private List<Reply> replies;
    private Pageable pageable;

    public QueryRecordDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getUpdateTimeBefore() {
        return updateTimeBefore;
    }

    public void setUpdateTimeBefore(Date updateTimeBefore) {
        this.updateTimeBefore = updateTimeBefore;
    }

    public Date getUpdateTimeAfter() {
        return updateTimeAfter;
    }

    public void setUpdateTimeAfter(Date updateTimeAfter) {
        this.updateTimeAfter = updateTimeAfter;
    }

    public Boolean getPv() {
        return pv;
    }

    public void setPv(Boolean pv) {
        this.pv = pv;
    }

    public Long getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(Long publishUserId) {
        this.publishUserId = publishUserId;
    }

    public Long getCatalogId() {
        return catalogId;
    }

    public void setCatalogId(Long catalogId) {
        this.catalogId = catalogId;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public List<Reply> getReplies() {
        return replies;
    }

    public void setReplies(List<Reply> replies) {
        this.replies = replies;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }
}

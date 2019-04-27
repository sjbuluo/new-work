package com.sun.health.newwork.record.dto;

import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Date;

public class QueryCatalogDTO implements Serializable {

    private static final long serialVersionUID = 7731038647356136181L;

    private Long userId;

    private String name;

    private Long parentId;

    private Boolean pv;

    private Date createTimeBefore;

    private Date createTimeAfter;

    private Pageable pageable;

    public QueryCatalogDTO() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Boolean isPv() {
        return pv;
    }

    public void setPv(Boolean pv) {
        this.pv = pv;
    }

    public Date getCreateTimeBefore() {
        return createTimeBefore;
    }

    public void setCreateTimeBefore(Date createTimeBefore) {
        this.createTimeBefore = createTimeBefore;
    }

    public Date getCreateTimeAfter() {
        return createTimeAfter;
    }

    public void setCreateTimeAfter(Date createTimeAfter) {
        this.createTimeAfter = createTimeAfter;
    }

    public Pageable getPageable() {
        return pageable;
    }

    public void setPageable(Pageable pageable) {
        this.pageable = pageable;
    }
}

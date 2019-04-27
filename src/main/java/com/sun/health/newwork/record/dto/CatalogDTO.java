package com.sun.health.newwork.record.dto;

import com.sun.health.newwork.base.dto.UserDTO;

import java.io.Serializable;
import java.util.Date;

public class CatalogDTO implements Serializable {

    private static final long serialVersionUID = 4968324363410493498L;

    private Long id;

    private Long userId;

    private String name;

    private String description;

    private Date createTime;

    private Long parentId;

    private boolean pv;

    private String username;

    public CatalogDTO() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public boolean isPv() {
        return pv;
    }

    public void setPv(boolean pv) {
        this.pv = pv;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

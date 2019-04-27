package com.sun.health.newwork.record.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.health.newwork.base.entity.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "catalog")
public class Catalog {

    private Long id;

    private String name;

    private String description;

    private Date createTime;

    private Catalog parentCatalog;

    private Set<Catalog> subCatalogs;

    private boolean pv;

    private User createUser;

    private Set<Record> recordSet;

    public Catalog() {
    }

    public Catalog(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Catalog(Long id, String name, String description, Date createTime, boolean pv, Long parentId, String parentCatalogName, Long userId, String username) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createTime = createTime;
        this.parentCatalog = parentId == null ? null : new Catalog(parentId, parentCatalogName);
        this.pv = pv;
        this.createUser = new User(userId, username);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "create_time")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @ManyToOne()
    @JoinColumn(name = "parentId")
    public Catalog getParentCatalog() {
        return parentCatalog;
    }

    public void setParentCatalog(Catalog parentCatalog) {
        this.parentCatalog = parentCatalog;
    }

    @OneToMany(mappedBy = "parentCatalog", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    public Set<Catalog> getSubCatalogs() {
        return subCatalogs;
    }

    @JsonIgnore
    public void setSubCatalogs(Set<Catalog> subCatalogs) {
        this.subCatalogs = subCatalogs;
    }

    @Column(name = "private")
    public boolean isPv() {
        return pv;
    }

    public void setPv(boolean pv) {
        this.pv = pv;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    @OneToMany(mappedBy = "catalog", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    public Set<Record> getRecordSet() {
        return recordSet;
    }

    @JsonIgnore
    public void setRecordSet(Set<Record> recordSet) {
        this.recordSet = recordSet;
    }
}

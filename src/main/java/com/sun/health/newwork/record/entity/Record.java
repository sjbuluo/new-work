package com.sun.health.newwork.record.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.health.newwork.base.entity.User;
import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "record")
public class Record {

    private Long id;

    private String title;

    private String content;

    private Date updateTime;

    private Date lastModifyTime;

    private boolean pv;

    private Catalog catalog;

    private User publishUser;

    @JsonIgnore
    private Set<Tag> tagSet;

    private Set<Tag> tagArr;

    private Set<Reply> replySet;

    public Record() {
    }

    public Record(Long id, String title, String content, Date updateTime, Date lastModifyTime, boolean pv, Long catalogId, String catalogName, Long createCatalogUserId, String createCatalogUsername, Long publishUserId, String publishUsername) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.updateTime = updateTime;
        this.lastModifyTime = lastModifyTime;
        this.pv = pv;
        this.catalog = new Catalog(catalogId, catalogName);
        this.catalog.setCreateUser(new User(createCatalogUserId, createCatalogUsername));
        this.publishUser = new User(publishUserId, publishUsername);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "content", columnDefinition = "text")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name = "update_time")
    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Column(name = "last_modify_time")
    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    @Column(name = "private")
    public boolean isPv() {
        return pv;
    }

    public void setPv(boolean pv) {
        this.pv = pv;
    }

    @ManyToOne()
    @JoinColumn(name = "catalog_id")
    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    @ManyToOne()
    @JoinColumn(name = "user_id")
    public User getPublishUser() {
        return publishUser;
    }

    public void setPublishUser(User publishUser) {
        this.publishUser = publishUser;
    }

    //    @OneToMany(mappedBy = "", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinTable(name = "record_tag",
        inverseJoinColumns = {
            @JoinColumn(name = "tag_id")
        },
        joinColumns = {
            @JoinColumn(name = "record_id")
        })
    public Set<Tag> getTagSet() {
        return tagSet;
    }

    @Transient
    public Set<Tag> getTagArr() {
        return tagArr;
    }

    public void setTagArr(Set<Tag> tagArr) {
        this.tagArr = tagArr;
    }

    public void setTagSet(Set<Tag> tagSet) {
        this.tagSet = tagSet;
    }

    @OneToMany(mappedBy = "replyRecord", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    public Set<Reply> getReplySet() {
        return replySet;
    }

    public void setReplySet(Set<Reply> replySet) {
        this.replySet = replySet;
    }
}

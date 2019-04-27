package com.sun.health.newwork.record.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.health.newwork.base.entity.User;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
@Table(name = "tag")
public class Tag {

    private Long id;

    private String name;

    private String description;

    private Date createTime;

    @JsonIgnore
    private User createUser;

    @JsonIgnore
    private Set<Record> recordSet;

//    private Record record;

    public Tag() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name", unique = true)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    @ManyToMany(mappedBy = "tagSet", fetch = FetchType.LAZY)
    public Set<Record> getRecordSet() {
        return recordSet;
    }

    public void setRecordSet(Set<Record> recordSet) {
        this.recordSet = recordSet;
    }

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "record_id")
//    public Record getRecord() {
//        return record;
//    }
//
//    @JsonIgnore
//    public void setRecord(Record record) {
//        this.record = record;
//    }
}

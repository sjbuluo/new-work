package com.sun.health.newwork.record.entity;

import javax.persistence.*;

@Entity
@Table(name = "record_tag")
public class RecordTag {

    private Long id;

    private Long recordId;

    private Long tagId;

    public RecordTag() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "record_id")
    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
        this.recordId = recordId;
    }

    @Column(name = "tag_id")
    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}

package com.sun.health.newwork.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.health.newwork.record.entity.Catalog;
import com.sun.health.newwork.record.entity.Record;
import com.sun.health.newwork.record.entity.Reply;
import com.sun.health.newwork.record.entity.Tag;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

//@JsonIgnoreProperties({"catalogSet", "recordSet", "tagSet", "recordSet"})
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class User implements Serializable {

    private Long id;

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private Set<Catalog> catalogSet;

    private Set<Record> recordSet;

    private Set<Tag> tagSet;

    private Set<Reply> replySet;

    public User() {
    }

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public User(Long id, String username, String password, String email, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "username", unique = true)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "email", unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "phone_number", unique = true)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @OneToMany(mappedBy = "createUser", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH}, targetEntity = Catalog.class)
    public Set<Catalog> getCatalogSet() {
        return catalogSet;
    }

    @JsonIgnore
    public void setCatalogSet(Set<Catalog> catalogSet) {
        this.catalogSet = catalogSet;
    }

    @OneToMany(mappedBy = "publishUser", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH}, targetEntity = Record.class)
    public Set<Record> getRecordSet() {
        return recordSet;
    }

    @JsonIgnore
    public void setRecordSet(Set<Record> recordSet) {
        this.recordSet = recordSet;
    }

    @OneToMany(mappedBy = "createUser", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH}, targetEntity = Tag.class)
    public Set<Tag> getTagSet() {
        return tagSet;
    }

    @JsonIgnore
    public void setTagSet(Set<Tag> tagSet) {
        this.tagSet = tagSet;
    }

    @OneToMany(mappedBy = "replyUser", fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH}, targetEntity = Reply.class)
    public Set<Reply> getReplySet() {
        return replySet;
    }

    @JsonIgnore
    public void setReplySet(Set<Reply> replySet) {
        this.replySet = replySet;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}

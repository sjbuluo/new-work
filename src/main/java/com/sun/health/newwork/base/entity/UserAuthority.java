package com.sun.health.newwork.base.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_authority")
public class UserAuthority {

    private Long id;

    private Long userId;

    private Long authorityId;

    public UserAuthority() {
    }

    public UserAuthority(Long id, Long userId, Long authorityId) {
        this.id = id;
        this.userId = userId;
        this.authorityId = authorityId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "user_id")
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "authority_id")
    public Long getAuthorityId() {
        return authorityId;
    }

    public void setAuthorityId(Long authorityId) {
        this.authorityId = authorityId;
    }
}

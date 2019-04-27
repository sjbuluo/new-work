package com.sun.health.newwork.base.dto;

import java.io.Serializable;

public class AuthorityDTO implements Serializable {

    private static final long serialVersionUID = -6237410920088926159L;

    private Long id;

    private String name;

    private String description;

    public AuthorityDTO() {
    }

    public AuthorityDTO(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}

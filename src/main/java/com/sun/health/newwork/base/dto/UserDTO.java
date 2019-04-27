package com.sun.health.newwork.base.dto;

import java.io.Serializable;
import java.util.List;

public class UserDTO implements Serializable {

    private static final long serialVersionUID = -3584863096448685466L;

    private Long id;

    private String username;

    private String password;

    private String email;

    private String phoneNumber;

    private List<AuthorityDTO> authorities;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String password, String email, String phoneNumber, List<AuthorityDTO> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.authorities = authorities;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<AuthorityDTO> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<AuthorityDTO> authorities) {
        this.authorities = authorities;
    }
}

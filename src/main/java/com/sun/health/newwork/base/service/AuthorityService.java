package com.sun.health.newwork.base.service;

import com.sun.health.newwork.base.entity.Authority;

import java.util.List;

public interface AuthorityService {

    List<Authority> findAuthoritiesByUserId(Long userId);

    List<String> findAuthorityNameListByUserId(Long userId);

    Authority saveAuthority(Authority authority);

    Authority findByName(String name);

    Authority findById(Long id);

}

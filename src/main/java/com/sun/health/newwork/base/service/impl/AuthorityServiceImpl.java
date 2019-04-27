package com.sun.health.newwork.base.service.impl;

import com.sun.health.newwork.base.entity.Authority;
import com.sun.health.newwork.base.repository.AuthorityRepository;
import com.sun.health.newwork.base.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public List<Authority> findAuthoritiesByUserId(Long userId) {
        return authorityRepository.queryAuthoritiesOfUser(userId);
    }

    @Override
    public List<String> findAuthorityNameListByUserId(Long userId) {
        return authorityRepository.queryAuthorityNameListOfUser(userId);
    }

    @Override
    public Authority saveAuthority(Authority authority) {
        return authorityRepository.save(authority);
    }

    @Override
    public Authority findByName(String name) {
        return authorityRepository.findByName(name);
    }

    @Override
    public Authority findById(Long id) {
        return authorityRepository.findById(id).get();
    }
}

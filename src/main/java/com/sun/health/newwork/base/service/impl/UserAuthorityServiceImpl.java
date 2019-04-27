package com.sun.health.newwork.base.service.impl;

import com.sun.health.newwork.base.entity.UserAuthority;
import com.sun.health.newwork.base.repository.UserAuthorityRepository;
import com.sun.health.newwork.base.service.UserAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserAuthorityServiceImpl implements UserAuthorityService {

    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    @Override
    public UserAuthority saveUserAuthority(UserAuthority userAuthority) {
        return userAuthorityRepository.save(userAuthority);
    }
}

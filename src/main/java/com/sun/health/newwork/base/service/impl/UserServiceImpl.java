package com.sun.health.newwork.base.service.impl;

import com.sun.health.newwork.base.entity.Authority;
import com.sun.health.newwork.base.entity.User;
import com.sun.health.newwork.base.entity.UserAuthority;
import com.sun.health.newwork.base.repository.UserRepository;
import com.sun.health.newwork.base.service.AuthorityService;
import com.sun.health.newwork.base.service.UserAuthorityService;
import com.sun.health.newwork.base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private UserAuthorityService userAuthorityService;

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findByUsernameAndEncodedPassword(String username, String encodedPassword) {
        return userRepository.findByUsernameAndPassword(username, encodedPassword);
    }

    @Override
    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        Authority authority = authorityService.findByName("user");
        UserAuthority userAuthority = new UserAuthority(null, savedUser.getId(), authority.getId());
        userAuthorityService.saveUserAuthority(userAuthority);
        return savedUser;
    }

    @Override
    public boolean existUsername(String username) {
        return userRepository.countByUsername(username) == 1;
    }

    @Override
    public boolean existEmail(String email) {
        return userRepository.countByEmail(email) == 1;
    }

    @Override
    public boolean existPhoneNumber(String phoneNumber) {
        return userRepository.countByPhoneNumber(phoneNumber) == 1;
    }
}

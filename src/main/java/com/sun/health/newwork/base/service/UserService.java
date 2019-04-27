package com.sun.health.newwork.base.service;

import com.sun.health.newwork.base.entity.User;

public interface UserService {

    User findByUsername(String username);

    User findByUsernameAndEncodedPassword(String username, String password);

    User saveUser(User user);

    boolean existUsername(String username);
    boolean existEmail(String email);
    boolean existPhoneNumber(String phoneNumber);


}

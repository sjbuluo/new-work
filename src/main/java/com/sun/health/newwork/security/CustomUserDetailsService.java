package com.sun.health.newwork.security;

import com.sun.health.newwork.base.entity.User;
import com.sun.health.newwork.base.service.AuthorityService;
import com.sun.health.newwork.base.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorityService authorityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("用户名(" + username + ")不存在");
        }
        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true, AuthorityUtils.
                createAuthorityList(StringUtils.join(authorityService.findAuthorityNameListByUserId(user.getId()), ","))
        );
    }
}

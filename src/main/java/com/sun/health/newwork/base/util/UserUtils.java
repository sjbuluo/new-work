package com.sun.health.newwork.base.util;

import com.sun.health.newwork.base.entity.User;
import com.sun.health.newwork.security.CustomUserDetails;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.file.attribute.UserPrincipalNotFoundException;

public class UserUtils {

    public static Long getSignInUserId() throws UserPrincipalNotFoundException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getId();
        } else {
            throw new UserPrincipalNotFoundException("尚未登录");
        }
    }

    public static User getSignInUser() throws UserPrincipalNotFoundException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof CustomUserDetails) {
            User user = new User();
            BeanUtils.copyProperties(principal,user);
            return user;
        } else {
            throw new UserPrincipalNotFoundException("尚未登录");
        }
    }

}

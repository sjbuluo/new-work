package com.sun.health.newwork.base.controller;

import com.sun.health.newwork.base.dto.ResponseDTO;
import com.sun.health.newwork.base.entity.User;
import com.sun.health.newwork.base.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
@RequestMapping("/sign")
public class SignController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @RequestMapping("/in/success")
    @ResponseBody
    public ResponseDTO inSuccess() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(0);
        responseDTO.setMessage("登录成功");
        responseDTO.setData(getPrincipal());
        return responseDTO;
    }

    @RequestMapping("/in/failure")
    @ResponseBody
    public ResponseDTO inFailure() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setCode(1);
        responseDTO.setMessage("登录失败");
        responseDTO.setError("账户名不存在或密码错误");
        return responseDTO;
    }

    @RequestMapping("/in/info")
    @ResponseBody
    public ResponseDTO inInfo() {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            if(getPrincipal() instanceof String) {
                responseDTO.setCode(1);
                responseDTO.setMessage("尚未登录");
                responseDTO.setData(null);
            } else {
                responseDTO.setCode(0);
                responseDTO.setMessage("查询登录信息成功");
                responseDTO.setData(getPrincipal());
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO.setCode(1);
            responseDTO.setMessage("查询登录信息失败");
            responseDTO.setData(null);
        }
        return responseDTO;
    }

    @RequestMapping("/up")
    @ResponseBody
    public ResponseDTO up(@RequestBody User user) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            System.out.println(user);
            User savedUser = userService.saveUser(user);
            responseDTO.setCode(0);
            responseDTO.setMessage("注册成功");
            responseDTO.setData(savedUser);
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO.setCode(1);
            responseDTO.setMessage("注册失败");
            responseDTO.setError("");
        }
        return responseDTO;
    }

    @RequestMapping("/up/exist/username")
    @ResponseBody
    public ResponseDTO existUsername(String username) {
        return new ResponseDTO(0, "查询成功", null, userService.existUsername(username));
    }

    @RequestMapping("/up/exist/email")
    @ResponseBody
    public ResponseDTO existEmail(String email) {
        return new ResponseDTO(0, "查询成功", null, userService.existEmail(email));
    }

    @RequestMapping("/up/exist/phoneNumber")
    @ResponseBody
    public ResponseDTO existPhoneNumber(String phoneNumber) {
        return new ResponseDTO(0, "查询成功", null, userService.existPhoneNumber(phoneNumber));
    }

    private Object getPrincipal() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}

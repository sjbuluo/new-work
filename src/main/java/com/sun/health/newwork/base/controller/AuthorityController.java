package com.sun.health.newwork.base.controller;

import com.sun.health.newwork.base.dto.ResponseDTO;
import com.sun.health.newwork.base.entity.Authority;
import com.sun.health.newwork.base.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/authority")
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    @RequestMapping("/save")
    @ResponseBody
    public ResponseDTO save(Authority authority) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            Authority savedAuthority = authorityService.saveAuthority(authority);
            responseDTO.setCode(0);
            responseDTO.setMessage("保存权限成功");
            responseDTO.setData(savedAuthority);
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO.setCode(1);
            responseDTO.setMessage("保存权限失败");
            responseDTO.setError(e.getMessage());
        }
        return responseDTO;
    }


}

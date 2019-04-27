package com.sun.health.newwork.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller()
@RequestMapping("/error")
public class ErrorController {

    @RequestMapping("/base")
    @ResponseBody
    public String base(String err) throws Exception {
        System.out.println(err);
        throw new Exception("统一异常处理");
//        return "";
    }

}

package com.sun.health.newwork.error;

import com.sun.health.newwork.base.dto.ResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class CustomErrorHandler {

    private Logger logger = LoggerFactory.getLogger(CustomErrorHandler.class);

    @ExceptionHandler(value = CustomException.class)
    public ModelAndView customExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        ModelAndView errorMV = new ModelAndView("error");
        errorMV.addObject("异常信息", e.getMessage());
        errorMV.addObject("异常详情", e);
        return errorMV;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseDTO<Exception> defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) {
        logger.error(request.getRequestURI() + "{}", e);
        String errParameter = request.getParameter("err");
        System.out.println(errParameter);
        return new ResponseDTO<>(1, "发生异常", e.getMessage(), e);
    }

}

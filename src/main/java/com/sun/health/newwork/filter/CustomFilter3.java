package com.sun.health.newwork.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Date;

@WebFilter(filterName = "CustomFilter3", urlPatterns = {"/*"})
@Order(3)
public class CustomFilter3 implements Filter {

    private Logger logger = LoggerFactory.getLogger(CustomFilter3.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("CustomFilter3 init {}", new Date());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("CustomFilter3 doFilter {}", new Date());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        logger.info("CustomFilter3 destroy {}", new Date());
    }
}

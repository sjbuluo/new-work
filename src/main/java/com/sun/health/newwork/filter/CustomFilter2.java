package com.sun.health.newwork.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.util.Date;

public class CustomFilter2 implements Filter {

    private Logger logger = LoggerFactory.getLogger(CustomFilter2.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("CustomFilter2 init {}", new Date());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        logger.info("CustomFilter2 doFilter {}", new Date());
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        logger.info("CustomFilter2 destroy {}", new Date());
    }
}

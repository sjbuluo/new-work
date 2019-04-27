package com.sun.health.newwork.filter;

import org.hibernate.internal.FilterConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class CustomFilterConfiguration {

    @Bean
    public FilterRegistrationBean registerFilter() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new CustomFilter());
        filterFilterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        filterFilterRegistrationBean.setName("CustomFilter");
        filterFilterRegistrationBean.setOrder(1);
        return filterFilterRegistrationBean;

    }


    @Bean
    public FilterRegistrationBean<Filter> registerFilter2() {
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new CustomFilter2());
        filterFilterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        filterFilterRegistrationBean.setName("CustomFilter2");
        filterFilterRegistrationBean.setOrder(2);
        return filterFilterRegistrationBean;
    }

    @Bean
    public Filter customFilter3Bean() {
        return new CustomFilter3();
    }

}

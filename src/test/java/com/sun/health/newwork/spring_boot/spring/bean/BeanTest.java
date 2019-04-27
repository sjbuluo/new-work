package com.sun.health.newwork.spring_boot.spring.bean;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.Filter;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BeanTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void test1() {
        Map<String, Filter> beansOfType = applicationContext.getBeansOfType(Filter.class);
        System.out.println(beansOfType);
    }

}

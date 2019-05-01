package com.sun.health.newwork.zookeeper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ZookeeperPropertiesTest {

    @Autowired
    private ZookeeperProperties zookeeperProperties;

    @Test
    public void test1() {
        System.out.println(zookeeperProperties.getHost());
    }

}

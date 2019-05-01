package com.sun.health.newwork.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CuratorFrameworkTest {

    @Autowired
    private CuratorFramework zkClient;

    @Test
    public void testLs() {
        try {
            List<String> children = zkClient.getChildren().forPath("/");
            for (String child : children) {
                System.out.println(child);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

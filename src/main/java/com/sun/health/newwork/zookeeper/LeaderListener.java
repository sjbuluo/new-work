package com.sun.health.newwork.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

public class LeaderListener extends LeaderSelectorListenerAdapter {

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    /**
     * 此方法执行完毕后 Leader会自动放弃身份
     * @param curatorFramework
     * @throws Exception
     */
    @Override
    public void takeLeadership(CuratorFramework curatorFramework) throws Exception {
        System.out.println("提供服务");
        countDownLatch.await();
    }
}

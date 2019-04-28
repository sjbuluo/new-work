package com.sun.health.newwork.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by 华硕 on 2019-04-28.
 */
public class ZKWatcherTest {

    private final static String ZK_SERVERS = "47.105.97.246:2181";

    private final static int TICK_TIME = 2000;

    private final static int CONNECT_TIMEOUT = TICK_TIME * 2;

    private final static int SESSION_TIMEOUT = TICK_TIME * 3;

    private ZooKeeper zooKeeper;
    private String path = "/from_java_with_zk";

    @Before
    public void createZookeeper() throws IOException {
        this.zooKeeper = new ZooKeeper(ZK_SERVERS, CONNECT_TIMEOUT, (WatchedEvent watchedEvent) -> {
            System.out.println("创建Zookeeper时添加的监听器");
            System.out.println(watchedEvent);
        });
        System.out.println(123);
    }

    @Test
    public void testWatcher() throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData(path, (WatchedEvent watchedEvent) -> {
            System.out.println("getData时添加的监听器");
        }, null);
        System.out.println(new String(data));
    }

    @After
    public void closeZookeeper() throws InterruptedException {
        if(zooKeeper != null) {
            zooKeeper.close();
        }
    }

}

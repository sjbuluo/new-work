package com.sun.health.newwork.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by 华硕 on 2019-04-28.
 */
public class ZKSimpleTest {

    private final static String ZK_SERVERS = "47.105.97.246:2181";

    private final static int TICK_TIME = 2000;

    private final static int CONNECT_TIMEOUT = TICK_TIME * 2;

    private ZooKeeper zooKeeper;
    private String path = "/from_java_with_zk_api";

    @Before
    public void createZookeeper() throws IOException {
        this.zooKeeper = new ZooKeeper(ZK_SERVERS, CONNECT_TIMEOUT, null);
    }

    @Test
    public void testExists() throws KeeperException, InterruptedException {
        Stat exists = this.zooKeeper.exists(path, null);
        System.out.println(exists);
    }

    @Test
    public void testLs() throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren("/", null);
        for (String child : children) {
            System.out.println(child);
        }
    }

    @Test
    public void testCreate() throws KeeperException, InterruptedException {
        String s = zooKeeper.create(path, "使用原生ZK API创建".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println(s);
    }

    @Test
    public void testGetData() throws KeeperException, InterruptedException, UnsupportedEncodingException {
        byte[] data = zooKeeper.getData(path, false, null);
        System.out.println(new String(data, "UTF-8"));
    }

    @Test
    public void testSetData() throws KeeperException, InterruptedException {
        Stat exists = zooKeeper.exists(path, null);
        Stat stat = zooKeeper.setData(path, "使用ZK原生API修改".getBytes(), exists.getVersion());
        System.out.println(stat);
    }

    @Test
    public void testDelete() throws KeeperException, InterruptedException {
        Stat exists = zooKeeper.exists(path, null);
        System.out.println(exists);
        zooKeeper.delete(path, exists.getVersion());
        exists = zooKeeper.exists(path, null);
        System.out.println(exists);
    }

    @After
    public void closeZookeeper() throws InterruptedException {
        if(zooKeeper != null) {
            zooKeeper.close();
        }
    }

}



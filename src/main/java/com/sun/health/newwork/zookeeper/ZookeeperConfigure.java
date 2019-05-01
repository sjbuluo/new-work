package com.sun.health.newwork.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ZookeeperProperties.class})
public class ZookeeperConfigure {

    private ZookeeperProperties zookeeperProperties;

    public ZookeeperConfigure(ZookeeperProperties zookeeperProperties) {
        this.zookeeperProperties = zookeeperProperties;
    }

    @Bean
    public CuratorFramework createCuratorFramework() {
        ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(zookeeperProperties.getTickTime(), zookeeperProperties.getMaxRetries());
        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient(zookeeperProperties.getHost() + ":" + zookeeperProperties.getPort(), exponentialBackoffRetry);
        curatorFramework.start();
        return curatorFramework;
    }

}

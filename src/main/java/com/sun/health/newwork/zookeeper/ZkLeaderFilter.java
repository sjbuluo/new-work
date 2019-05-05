package com.sun.health.newwork.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "zkLeaderFilter", urlPatterns = "/*")
@Order(1)
public class ZkLeaderFilter implements Filter {

    @Autowired
    private CuratorFramework zkClient;

    @Autowired
    private ZookeeperProperties zookeeperProperties;

    private LeaderSelector leaderSelector;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //实例化一个选举器
        // 特别注意LeaderListener，如果takeLeadership方法执行完毕，则会自动释放leaders身份，故我们使用countDownLatch来阻塞此方法使其不主动放弃leaders身份
        leaderSelector = new LeaderSelector(zkClient, zookeeperProperties.getLeaderPath(), new LeaderListener());
        leaderSelector.autoRequeue();
        leaderSelector.start();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if(leaderSelector.hasLeadership()) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            servletResponse.getWriter().write("是Follower");
        }
    }

    @Override
    public void destroy() {
        if(leaderSelector != null) {
            leaderSelector.close();
        }
    }
}

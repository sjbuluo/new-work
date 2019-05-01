package com.sun.health.newwork.zookeeper;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zk")
public class ZkCuratorController {

    @RequestMapping("/serve")
    public String serve() {
        return "是Leader";
    }

}

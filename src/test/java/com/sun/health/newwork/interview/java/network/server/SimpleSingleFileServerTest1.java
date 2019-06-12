package com.sun.health.newwork.interview.java.network.server;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by 华硕 on 2019-06-12.
 */
public class SimpleSingleFileServerTest1 {

    @Test
    public void test1() {
        String file = "hello.php";
        String contentType = "text/plain";
        String encoding = "UTF-8";
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] content = new byte[fis.available()];
            fis.read(content);
            SimpleSingleFileServer1 simpleSingleFileServer1 = new SimpleSingleFileServer1(contentType, content, encoding);
            simpleSingleFileServer1.startUp();
        } catch (Exception e) {

        }
    }

}

package com.sun.health.newwork.interview.java.network.server;

import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 华硕 on 2019-06-12.
 */
public class SimpleHttpRedirectorServer1 {

    private static class RedirectThread {

    }

    public void startup() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9362);
            while (true) {
                Socket accept = serverSocket.accept();
                new SocketRedirectThread(accept).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    public void test1() {
        new SimpleHttpRedirectorServer1().startup();
    }

}

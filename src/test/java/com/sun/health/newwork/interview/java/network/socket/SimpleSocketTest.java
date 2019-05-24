package com.sun.health.newwork.interview.java.network.socket;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by 华硕 on 2019-05-24.
 */
public class SimpleSocketTest {

    @Test
    public void test1() {
        try (Socket socket = new Socket("time.nist.gov", 13)) {
            socket.setSoTimeout(15000);
            try (InputStream in = socket.getInputStream(); InputStreamReader inr = new InputStreamReader(in); BufferedReader br = new BufferedReader(inr)) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        int port = 1;
        while(port++ < 1025) {
            try (Socket socket = new Socket("127.0.0.1", port)) {
                socket.setSoTimeout(5000);
                System.out.println(port + " 正在监听 运行服务");
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
//                e.printStackTrace();
                System.out.println(port + " 没有监听");
            }
        }
    }

    @Test
    public void test3() {
        try (Socket socket = new Socket("47.105.97.246", 6379)) {
            System.out.println(socket.getInetAddress());
            System.out.println(socket.getPort());
            System.out.println(socket.getLocalAddress());
            System.out.println(socket.getLocalPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

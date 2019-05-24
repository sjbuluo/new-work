package com.sun.health.newwork.interview.java.network.url_connection;


import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * Created by 华硕 on 2019-05-24.
 */
public class SimpleServerSocketTest {

    @Test
    public void test1() {
        try {
            ServerSocket serverSocket = new ServerSocket(6013);
            Socket accept = serverSocket.accept();
            System.out.println(123);
            accept.getOutputStream().write(new Date().toString().getBytes());
            accept.getOutputStream().flush();
            accept.getOutputStream().close();
            accept.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1_socket() {
        try {
            Socket socket = new Socket("localhost", 6013);
            try (InputStream in = socket.getInputStream(); InputStreamReader inputStreamReader = new InputStreamReader(in); BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                int c;
                while((c = bufferedReader.read()) != -1) {
                    System.out.print((char) c);
                }
                System.out.println();
            }
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

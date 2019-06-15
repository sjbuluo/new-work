package com.sun.health.newwork.netty.transform;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 华硕 on 2019-06-15.
 */
public class OIOWithoutOIO {

    @Test
    public void testServer() throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(9362);
            while (true) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    socket.getOutputStream().write("Hi!".getBytes());
                    socket.getOutputStream().flush();
                    socket.shutdownOutput();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (socket != null) {
                        socket.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }

    @Test
    public void testClient() {
        Socket socket = null;
        try {
            socket = new Socket("localhost", 9362);
            InputStream inputStream = socket.getInputStream();
            StringBuilder response = new StringBuilder();
            int c;
            while ((c = inputStream.read()) != -1) {
                response.append((char) c);
            }
            System.out.println(response.toString());
            socket.shutdownInput();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

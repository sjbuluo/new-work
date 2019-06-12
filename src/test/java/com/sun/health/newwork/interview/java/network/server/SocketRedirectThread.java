package com.sun.health.newwork.interview.java.network.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by 华硕 on 2019-06-12.
 */
public class SocketRedirectThread extends Thread {

    private Socket socket;

    public SocketRedirectThread() {
    }

    public SocketRedirectThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try(InputStream in = socket.getInputStream(); OutputStream out = socket.getOutputStream()) {
            StringBuilder firstLine = new StringBuilder();
            int c;
            while (true) {
                c = in.read();
                if (c == '\r' || c == '\n' || c == -1)
                    break;
                firstLine.append((char) c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

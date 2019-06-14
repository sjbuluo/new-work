package com.sun.health.newwork.interview.java.network.server;

import java.io.IOException;
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
            String[] split = firstLine.toString().split(" ");
            if (split.length < 2) {
                out.write("请使用浏览器访问".getBytes("UTF-8"));
                out.flush();
            } else {
                String path = split[1];
                String headerStr = "HTTP/1.1 302 FOUND\r\n" +
                        "Server: SJ Redirector\r\n" +
                        "Location: http://localhost:8080" + path + "\r\n" +
                        "\r\n";
                String bodyStr = "<html><head><title>重定向</title></head><body><div>从" + socket.getLocalAddress() + path + "到<a href=http://localhost:8080" + path + ">localhost:8080" +  path + "</a></div></body></html>";
                out.write(headerStr.getBytes("UTF-8"));
                out.write(bodyStr.getBytes("UTF-8"));
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (this.socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

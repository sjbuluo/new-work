package com.sun.health.newwork.interview.java.network.url_connection;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by 华硕 on 2019-05-24.
 */
public class Receiver {

    public static void main(String[] args) {
        List<Socket> sockets = new ArrayList<>();
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        try (ServerSocket serverSocket = new ServerSocket(6013)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println(socket.getInetAddress() + "连接");
                sockets.add(socket);
                executorService.submit(() -> {
                    while (true) {
                        try {
                            InputStream inputStream = socket.getInputStream();
                            if (inputStream.read() != -1) {
                                int c;
                                StringBuilder sb = new StringBuilder('\0');
                                while ((c = inputStream.read()) != '\0') {
                                    sb.append((char) c);
                                }
                                System.out.println(sb.toString());
                                sb.append('\0');
                                for (Socket s : sockets) {
                                    try {
                                        s.getOutputStream().write(sb.toString().getBytes());
                                        s.getOutputStream().flush();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        try {
                                            socket.shutdownInput();
                                            socket.shutdownOutput();
                                            socket.close();
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                        sockets.remove(socket);
                                    }
                                }
                            }
                            TimeUnit.MILLISECONDS.sleep(500);
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                socket.shutdownInput();
                                socket.shutdownOutput();
                                socket.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            sockets.remove(socket);
                            break;
                        }
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            for (Socket socket : sockets) {
                if (socket != null && socket.isConnected() && !socket.isClosed()) {
                    try {
                        socket.shutdownInput();
                        socket.shutdownOutput();
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (executorService != null) {
                executorService.shutdownNow();
            }
        }
    }

}

package com.sun.health.newwork.interview.java.network.url_connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 华硕 on 2019-05-27.
 */
@Deprecated
public class NIOSenderWrong1 {

    public static void main(String[] args) {
//        SocketChannel socketChannel = null;
//        Selector selector = null;
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        try {
            SocketChannel socketChannel = SocketChannel.open();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(9362);
            socketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            ByteBuffer allocate = ByteBuffer.allocate(32);
            socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ, allocate);
            socketChannel.connect(inetSocketAddress);
//            socketChannel.socket().connect(inetSocketAddress);
            executorService.submit(() -> {
                try {
                    while (true) {
                        selector.select();
                        Set<SelectionKey> selectionKeys = selector.selectedKeys();
                        Iterator<SelectionKey> iterator = selectionKeys.iterator();
                        while (iterator.hasNext()) {
                            SelectionKey selectionKey = iterator.next();
                            iterator.remove();
                            if (selectionKey.isConnectable()) {
                                SocketChannel socketChn = (SocketChannel) selectionKey.channel();
                                if (socketChn.isConnectionPending()) {
                                    if (socketChannel.isConnected()) {
                                        System.out.println("连接 " + socketChn.getRemoteAddress() + "成功");
//                                        socketChannel.register(selector, SelectionKey.OP_READ, allocate);
                                    }
                                }
                            }
                            if (selectionKey.isReadable()) {
                                SocketChannel channel = (SocketChannel) selectionKey.channel();
                                ByteBuffer attachment = (ByteBuffer) selectionKey.attachment();
                                int read = channel.read(attachment);
                                byte[] data = new byte[read];
                                attachment.get(data, 0, read);
                                System.out.println(new String(data, "UTF-8"));
//                                attachment.clear();
                            }
//                            if (selectionKey.isWritable()) {
//                                SocketChannel channel = (SocketChannel) selectionKey.channel();
//                                ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
//                                byteBuffer.flip();
//                                channel.write(byteBuffer);
//                                byteBuffer.compact();
//                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            Scanner scanner = new Scanner(System.in);
            String line = null;
            while (!"exit".equalsIgnoreCase(line)) {
                line = scanner.nextLine();
                ByteBuffer wrap = ByteBuffer.wrap(line.getBytes("UTF-8"));
//                allocate.put(wrap);
//                socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, allocate);
                wrap.flip();
                socketChannel.write(wrap);
                wrap.compact();
//                socketChannel.register(selector, SelectionKey.OP_READ, allocate);
            }
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if(executorService != null) {
                executorService.shutdownNow();
            }
        }

    }

}

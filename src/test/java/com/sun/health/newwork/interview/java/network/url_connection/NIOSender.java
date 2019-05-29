package com.sun.health.newwork.interview.java.network.url_connection;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
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
public class NIOSender {

    @Test
    public void test1() {
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(9362));
            ByteBuffer wrap = ByteBuffer.wrap("Hello World".getBytes());
            socketChannel.write(wrap);
            wrap.compact();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void main(String[] args) {
        final SocketChannel socketChannel;
        final Selector selector;
        final SelectionKey selectionKey;
        final Scanner scanner;
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        try {
            socketChannel = SocketChannel.open();
            selector = Selector.open();
            socketChannel.configureBlocking(false);
            ByteBuffer allocate = ByteBuffer.allocate(32);
            selectionKey = socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ, allocate);
            executorService.submit(() -> {
                while (true) {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                        if (key.isValid() && key.isConnectable()) {
                            System.out.println("isConnectionPending" + channel.isConnectionPending());
                            System.out.println("isConnected" + channel.isConnected());
                            System.out.println("连接成功 " + channel.getRemoteAddress());
                        }
                        if (key.isValid() && key.isReadable()) {
                            int read = channel.read(byteBuffer);
                            if (read == -1) {
                                System.out.println("关闭连接");
                                break;
                            } else {
                                byte[] bytes = new byte[read];
                                channel.read(byteBuffer);
                                byteBuffer.flip();
                                byteBuffer.get(bytes, 0, read);
                                System.out.println(new String(bytes));
                                byteBuffer.compact();
                                byteBuffer.clear();
                            }
                        }
                        iterator.remove();
                    }
                }
            });
            socketChannel.connect(new InetSocketAddress(9362));
            if (socketChannel.finishConnect()) {
                String line = null;
                scanner = new Scanner(System.in);
                while (!"exit".equalsIgnoreCase(line)) {
                    line = scanner.nextLine();
                    ByteBuffer byteBuffer = ByteBuffer.wrap(line.getBytes());
//                    byteBuffer.flip();
                    socketChannel.write(byteBuffer);
                    byteBuffer.compact();
                    byteBuffer.clear();
                }
            }
            if (selectionKey != null) {
                selectionKey.cancel();
            }
            if (selector != null) {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (executorService != null) {
                try {
                    executorService.shutdownNow();
                } catch (Exception e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

}

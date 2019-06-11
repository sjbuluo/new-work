package com.sun.health.newwork.interview.java.network.socket;

import javassist.bytecode.ByteArray;
import org.bson.ByteBuf;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 华硕 on 2019-06-10.
 */
public class NIOSenderReview1 {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        try (SocketChannel socketChannel = SocketChannel.open();
             Selector selector = Selector.open();) {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(9362);
            socketChannel.configureBlocking(false);
            ByteBuffer byteBuffer = ByteBuffer.allocate(32);
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_CONNECT | SelectionKey.OP_READ, byteBuffer);
            executorService.submit(() -> {
                try {
                    boolean isConnected = true;
                    while (isConnected) {
                        selector.select();
                        Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                        while (selectionKeyIterator.hasNext()) {
                            SelectionKey key = selectionKeyIterator.next();
                            if (key.isValid() && key.isConnectable()) {
                                SocketChannel channel = (SocketChannel) key.channel();
                                System.out.println("isConnectionPending" + channel.isConnectionPending());
                                System.out.println("isConnected" + channel.isConnected());
                                System.out.println("连接" + channel.getRemoteAddress() + "成功");
                            } else if (key.isValid() && key.isReadable()) {
                                SocketChannel channel = (SocketChannel) key.channel();
                                ByteBuffer buffer = (ByteBuffer) key.attachment();
                                try {
                                    int read = channel.read(buffer);
                                    if (read == -1) {
                                        isConnected = false;
                                        System.out.println("断开");
                                        break;
                                    }
                                    byte[] bytes = new byte[read];
                                    System.out.println(buffer);
                                    buffer.flip();
                                    System.out.println(buffer);
                                    buffer.get(bytes, 0, read);
                                    System.out.println("接受: " + new String(bytes, "UTF-8"));
                                    byteBuffer.compact();
                                    byteBuffer.clear();
                                } catch (IOException e) {
                                    isConnected = false;
                                    System.out.println(e.getMessage());
                                    System.out.println("断开");
                                }
                            }
                            selectionKeyIterator.remove();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("断开");
                }
            });
            socketChannel.connect(inetSocketAddress);
            if (socketChannel.isConnectionPending()) {
            }
            if (socketChannel.finishConnect()) {
                Scanner scanner = new Scanner(System.in);
                String line = null;
                while (true) {
                    line = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(line)) {
                        System.out.println("退出");
                        break;
                    }
                    line = "FROM " + socketChannel.getLocalAddress() + ": " + line;
                    ByteBuffer wrap = ByteBuffer.wrap(line.getBytes());
//                    wrap.flip();
                    System.out.println(wrap);
                    try {
                        socketChannel.write(wrap);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        System.out.println("断开");
                        break;
                    }
                    wrap.compact();
                    System.out.println(wrap);
                    wrap.clear();
                    System.out.println(wrap);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (executorService != null) {
                executorService.shutdownNow();
            }
        }
    }


}

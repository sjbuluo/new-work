package com.sun.health.newwork.interview.java.network.url_connection;


import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by 华硕 on 2019-05-27.
 */
public class NIOServer {

    @Test
    public void test1() {
        ServerSocketChannel serverSocketChannel = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(9362));
            SocketChannel socketChannel = serverSocketChannel.accept();
            ByteBuffer byteBuffer = ByteBuffer.allocate(32);
            int read = socketChannel.read(byteBuffer);
            byte[] data = new byte[read];
            byteBuffer.flip();
            byteBuffer.get(data, 0, read);
            System.out.println(new String(data));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (serverSocketChannel != null) {
                try {
                    serverSocketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        SelectionKey selectionKey = null;
        ExecutorService executorService = null;
        List<SocketChannel> socketChannels = new LinkedList<>();
        List<SelectionKey> selectionKeys = new ArrayList<>();
        try {
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.socket().bind(new InetSocketAddress(9362));
            selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                if (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isValid() && key.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = channel.accept();
                        System.out.println(socketChannel.getRemoteAddress() + " 连接");
                        socketChannel.configureBlocking(false);
                        socketChannels.add(socketChannel);
                        SelectionKey register = socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(32));
                        selectionKeys.add(register);
                    }
                    if (key.isValid() && key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                        try {
                            if (channel.isConnected()) {
                                int read = channel.read(byteBuffer);
                                if (read == -1) {
                                    System.out.println(channel.getRemoteAddress() + " 关闭连接");
                                    channel.close();
                                    socketChannels.remove(channel);
                                    continue;
                                }
                                byte[] data = new byte[read];
                                byteBuffer.flip();
                                byteBuffer.get(data, 0, read);
                                System.out.println(new String(data));
                                for (SocketChannel socketChannel : socketChannels) {
                                    try {
                                        byteBuffer.flip();
                                        socketChannel.write(byteBuffer);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        socketChannels.remove(socketChannel);
                                    }
                                }
                                byteBuffer.compact();
                                byteBuffer.clear();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            socketChannels.remove(channel);
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
            if (serverSocketChannel != null) {
                try {
                    serverSocketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            for (SelectionKey key : selectionKeys) {
                key.cancel();
            }
            for (SocketChannel socketChannel : socketChannels) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

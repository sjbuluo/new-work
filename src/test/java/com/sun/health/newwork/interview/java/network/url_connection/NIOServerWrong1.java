package com.sun.health.newwork.interview.java.network.url_connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

/**
 * Created by 华硕 on 2019-05-27.
 */
@Deprecated
public class NIOServerWrong1 {

    public static void main(String[] args) {
        ServerSocketChannel serverSocketChannel = null;
        List<SocketChannel> senderList = new LinkedList<>();
        List<SelectionKey> keyList = new LinkedList<>();
        try {
            serverSocketChannel = ServerSocketChannel.open();
            ServerSocket serverSocket = serverSocketChannel.socket();
            InetSocketAddress inetSocketAddress = new InetSocketAddress(9362);
            serverSocket.bind(inetSocketAddress);
            serverSocketChannel.configureBlocking(false);
            Selector selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, ByteBuffer.allocate(32));
            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel serverSocketChn = (ServerSocketChannel) selectionKey.channel();
                        SocketChannel socketChannel = serverSocketChn.accept();
                        System.out.println(socketChannel.getRemoteAddress() + " 连接");
                        senderList.add(socketChannel);
                        socketChannel.configureBlocking(false);
                        SelectionKey register = socketChannel.register(selector, SelectionKey.OP_READ);
                        keyList.add(register);
                        ByteBuffer allocate = ByteBuffer.allocate(32);
                        register.attach(allocate);
                    }
                    if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                        int read = socketChannel.read(byteBuffer);
                        byte[] bytes = new byte[read];
                        System.out.println(byteBuffer.get(bytes, 0, read));
                        for (SocketChannel channel : senderList) {
                            try {
                                byteBuffer.flip();
                                channel.write(byteBuffer);
                            } catch (Exception e) {
                                senderList.remove(senderList);
                            }
                        }
                        byteBuffer.compact();
                    }
                }
            }
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
            for (SelectionKey selectionKey : keyList) {
                selectionKey.cancel();
            }
            for (SocketChannel socketChannel : senderList) {
                try {
                    socketChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

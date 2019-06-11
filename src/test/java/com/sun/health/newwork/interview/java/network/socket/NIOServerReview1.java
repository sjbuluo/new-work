package com.sun.health.newwork.interview.java.network.socket;

import org.bson.ByteBuf;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 华硕 on 2019-06-10.
 */
public class NIOServerReview1 {

    public static void main(String[] args) {


//        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Set<SocketChannel> socketChannelSet = new HashSet<>();
        Set<SelectionKey> selectionKeySet = new HashSet<>();
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
             Selector selector = Selector.open();) {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(9362);
            serverSocketChannel.configureBlocking(false);
            ByteBuffer byteBuffer = ByteBuffer.allocate(32);
            SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, byteBuffer);
            serverSocketChannel.socket().bind(inetSocketAddress);
            
            while (true) {
                selector.select();
                Iterator<SelectionKey> selectionKeyIterator = selector.selectedKeys().iterator();
                while (selectionKeyIterator.hasNext()) {
                    SelectionKey key = selectionKeyIterator.next();
                    if (key.isValid() && key.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        SocketChannel accept = channel.accept();
                        if (accept.isConnectionPending()) {

                        }
                        if (accept.finishConnect()) {
                            accept.configureBlocking(false);
                            SelectionKey register = accept.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(32));
                            socketChannelSet.add(accept);
                            selectionKeySet.add(register);
                            System.out.println("接受" + accept.getRemoteAddress() + "成功");
                        }
                    } else if (key.isValid() && key.isReadable()) {
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        if (channel.isConnected()) {
                            int read = -1;
                            try {
                                read = channel.read(buffer);
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                            if (read == -1) {
                                System.out.println("断开: " + channel.getRemoteAddress());
                                socketChannelSet.remove(channel);
                                channel.close();
                            } else {
                                byte[] bytes = new byte[read];
                                buffer.flip();
                                buffer.get(bytes, 0, read);
                                System.out.println("收到消息: " + new String(bytes, "UTF-8"));
                                for (SocketChannel socketChannel : socketChannelSet) {
                                    buffer.flip();
                                    try {
                                        socketChannel.write(buffer);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        socketChannelSet.remove(socketChannel);
                                        if (socketChannel != null) {
                                            socketChannel.close();
                                        }
                                    }
                                }
                                buffer.compact();
                                buffer.clear();
                            }
                        }

                    } else if (key.isValid() && key.isConnectable()) {
                        System.out.println("连接");
                    }
                    selectionKeyIterator.remove();
                }
            }
            

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            for (SocketChannel channel : socketChannelSet) {
                if (channel != null) {
                    try {
                        channel.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    
}

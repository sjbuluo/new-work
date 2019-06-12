package com.sun.health.newwork.interview.java.network.server;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by 华硕 on 2019-06-12.
 */
public class SimpleSingleFileServer1 {

    private int contentLength;

    private String contentType;

    private byte[] content;

    private byte[] headers;

    private String encoding;

    public SimpleSingleFileServer1() {
    }

    public SimpleSingleFileServer1(String contentType, byte[] content, String encoding) throws UnsupportedEncodingException {
        this.contentType = contentType;
        this.content = content;
        this.contentLength = content.length;
        this.encoding = encoding;
        this.headers =
                ("HTTP/1.1 200 OK\r\n" +
                 "Server: SJ File Server Alpha\r\n" +
                 "Content-Length: " + this.contentLength + "\r\n" +
                 "Content-Type: " + this.contentType + "\r\n" +
                 "\r\n")
                .getBytes(this.encoding);
    }

    public void startUp() {
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        SelectionKey selectionKey = null;
        try {
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, ByteBuffer.allocate(32));
            serverSocketChannel.socket().bind(new InetSocketAddress(9362));
            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    if (next.isValid() && next.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) next.channel();
                        ByteBuffer data = (ByteBuffer) next.attachment();
                        SocketChannel accept = channel.accept();
                        if (accept.isConnectionPending()) {

                        }
                        if (accept.finishConnect()) {
                            try (InputStream is = accept.socket().getInputStream(); BufferedInputStream bis = new BufferedInputStream(is); OutputStream os = accept.socket().getOutputStream()) {
                                StringBuilder firstLine = new StringBuilder();
                                while (true) {
                                    int c = is.read();
                                    if (c == '\r' || c == '\n' || c == -1)
                                        break;
                                    firstLine.append((char) c);
                                }
                                if (firstLine.indexOf("HTTP/") != -1) {
                                    os.write(headers);
                                }
                                os.write(content);
                                os.flush();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (accept != null) {
                            accept.close();
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (Exception e) {

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
        }
    }

}

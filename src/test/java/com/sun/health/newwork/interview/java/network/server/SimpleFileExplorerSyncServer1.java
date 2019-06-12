package com.sun.health.newwork.interview.java.network.server;

import org.junit.Test;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 华硕 on 2019-06-12.
 */
public class SimpleFileExplorerSyncServer1 {

    public void startup() {
        ServerSocketChannel serverSocketChannel = null;
        Selector selector = null;
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        try {
            serverSocketChannel = ServerSocketChannel.open();
            selector = Selector.open();
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            serverSocketChannel.socket().bind(new InetSocketAddress(9362));
            while (true) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    if (next.isValid() && next.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) next.channel();
                        executorService.submit(() -> {
                            final SocketChannel accept;
                            try {
                                accept = channel.accept();
                            } catch (IOException e) {
                                e.printStackTrace();
                                return;
                            }
                            if (accept.isConnectionPending()) {

                            }
                            try {
                                if (accept.finishConnect()) {
                                    String dirOrFile = "F:";
                                    try (InputStream in = accept.socket().getInputStream(); BufferedInputStream buIn = new BufferedInputStream(in); OutputStream out = accept.socket().getOutputStream()) {
                                        StringBuilder result = new StringBuilder();
                                        StringBuilder firstLine = new StringBuilder();
                                        int c;
                                        while (true) {
                                            c = buIn.read();
                                            if (c == '\r' || c == '\n' || c == -1) {
                                                break;
                                            }
                                            firstLine.append((char) c);
                                        }
                                        String[] split = firstLine.toString().split(" ");
                                        if (split.length < 2) {
                                            result.append("请使用浏览器访问");
                                        } else {
                                            dirOrFile += URLDecoder.decode(split[1], "UTF-8");
                                            File file = new File(dirOrFile);
                                            if (file.exists()) {
                                                if (file.isDirectory()) {
                                                    result.append("HTTP/1.1 200 OK\r\n" +
                                                            "Server: SJ File Explorer \r\n" +
                                                            "Content-Type: text/html;charset=utf-8\r\n" +
                                                            "\r\n");
                                                    result.append("<html><head><title>文件浏览</title></head><body>");
                                                    if (dirOrFile.length() > 3) {
                                                        result.append("<a href=\"../\">../</a>");
                                                    }
                                                    String[] list = file.list();
                                                    for (String s : list) {
                                                        result.append("<div><a href=\"");
                                                        result.append(s);
                                                        result.append("/\">");
                                                        result.append(s);
                                                        result.append("</a></div>");
                                                    }
                                                    result.append("</body></html>");
                                                    out.write(result.toString().getBytes("UTF-8"));
                                                    out.flush();
                                                    return;
                                                } else {
                                                    if (file.length() > (1024 * 1024 * 20)) {
                                                        result.append("文件过大");
                                                    } else {
                                                        try (FileInputStream fileInputStream = new FileInputStream(file)) {
                                                            int available = fileInputStream.available();
                                                            byte[] bytes = new byte[available];
                                                            fileInputStream.read(bytes);
                                                            String contentType = Files.probeContentType(file.toPath());
                                                            boolean isDownload = contentType == null || (contentType.indexOf("image") == -1 && contentType.indexOf("text") == -1);
                                                            String headersStr = "HTTP/1.1 200 OK\r\n" +
                                                                    "Server: SJ File Explorer \r\n" +
                                                                    "Content-Type: " + contentType + "  \r\n" +
                                                                    "Content-Length: " + available + "\r\n" +
                                                                    (isDownload ? ("Content-Disposition: attachment;filename=" + file.getName() + "\r\n") : "") +
                                                                    "\r\n";
                                                            out.write(headersStr.getBytes("UTF-8"));
                                                            out.write(bytes);
                                                            out.flush();
                                                            return;
                                                        }
                                                    }

                                                }
                                            } else {
                                                result.append("目录或文件不存在");
                                            }
                                        }
                                        out.write(("HTTP/1.1 200 OK\r\n" +
                                                "Server: SJ File Explorer \r\n" +
                                                "Content-Type: text/plain\r\n" +
                                                "\r\n" + result.toString()).getBytes("UTF-8"));
                                        out.flush();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    if (accept != null) {
                                        try {
                                            accept.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    iterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (executorService != null) {
                executorService.shutdownNow();
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

    @Test
    public void test1() {
        new SimpleFileExplorerSyncServer1().startup();
    }
}

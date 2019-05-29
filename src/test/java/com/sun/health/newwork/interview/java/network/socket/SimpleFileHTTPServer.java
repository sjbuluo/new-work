package com.sun.health.newwork.interview.java.network.socket;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 华硕 on 2019-05-28.
 */
public class SimpleFileHTTPServer {

    final private int port;

    final private byte[] content;

    final private byte[] header;

    final private String encoding;

    public SimpleFileHTTPServer(byte[] content, String encoding, String mimeType, int port) {
        this.content = content;
        String headerStr = "" +
                "HTTP/1.1 200 OK\r\n" +
                "Server: OneFile 2.0\r\n" +
                "Content-length: " + content.length + "\r\n" +
                "Content-type: " + mimeType + "\r\n\r\n";
        this.header = headerStr.getBytes(Charset.forName("US-ASCII"));
        this.encoding = encoding;
        this.port = port;
    }

    public void start() {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                pool.submit(handleResponse(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pool != null) {
                pool.shutdownNow();
            }
        }
    }

    private Callable<Void> handleResponse(final Socket socket) {
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                try (InputStream inputStream = socket.getInputStream(); BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream); OutputStream outputStream = socket.getOutputStream()) {
                    int c = '\0';
                    StringBuilder stringBuilder = new StringBuilder();
                    while (true) {
                        c = bufferedInputStream.read();
                        if (c == '\r' || c == '\n' || c == -1)
                            break;
                        stringBuilder.append((char) c);
                    }
                    if (stringBuilder.toString().indexOf("HTTP/") != -1) {
                        outputStream.write(header);
                    }
                    outputStream.write(content);
                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
    }

    public static void main(String[] args) throws IOException {
        String filename = "Java网络编程.txt";
        int port = 9362;
        String encoding = "UTF-8";
        if (args.length > 0)
            filename = args[0];
        if (args.length > 1)
            port = Integer.parseInt(args[1]);
        if (args.length > 2)
            encoding = args[2];
        byte[] content = Files.readAllBytes(Paths.get(filename));
        String contentType = URLConnection.getFileNameMap().getContentTypeFor(filename);
        SimpleFileHTTPServer simpleFileHTTPServer = new SimpleFileHTTPServer(content, encoding, contentType, port);
        simpleFileHTTPServer.start();
    }

}

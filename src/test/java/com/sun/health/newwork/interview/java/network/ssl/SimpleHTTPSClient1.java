package com.sun.health.newwork.interview.java.network.ssl;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

/**
 * Created by 华硕 on 2019-06-13.
 */
public class SimpleHTTPSClient1 {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No Host");
            return ;
        }
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = null;
        try {
            String host = args[0];
            sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, 443);
            String[] supportedCipherSuites = sslSocket.getSupportedCipherSuites();
            sslSocket.setEnabledCipherSuites(supportedCipherSuites);
            try (OutputStream outputStream = sslSocket.getOutputStream();
                 OutputStreamWriter writer = new OutputStreamWriter(outputStream);
                 BufferedWriter bufferedWriter = new BufferedWriter(writer);
                 InputStream inputStream = sslSocket.getInputStream();
                 InputStreamReader reader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(reader)) {
                bufferedWriter.write("GET https://" + host+ "/ HTTP/1.1\r\n");
                bufferedWriter.write("Host: " + host + "\r\n");
                bufferedWriter.write("\r\n");
                bufferedWriter.flush();
                StringBuilder response = new StringBuilder();
                String line = null;
                while (!"".equalsIgnoreCase(line)) {
                    line = bufferedReader.readLine();
                    System.out.println(line);
                }
                String contentLengthLine = bufferedReader.readLine();
                int contentLength = Integer.MAX_VALUE;
                try {
                    contentLength = Integer.parseInt(contentLengthLine, 16);
                    System.out.println("Content-Length: " + contentLength);
                } catch (NumberFormatException e) {
                    System.out.println("No ContentLength");
                    System.out.println(contentLengthLine);
                }
                int c;
                int i = 0;
                while ((c = bufferedReader.read()) != -1 && i++ < contentLength) {
                    response.append((char) c);
                }
                System.out.println(response.toString());
            } catch (IOException e) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sslSocket != null) {
                try {
                    sslSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}

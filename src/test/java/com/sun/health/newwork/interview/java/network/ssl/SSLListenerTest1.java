package com.sun.health.newwork.interview.java.network.ssl;

import org.junit.Test;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Created by 华硕 on 2019-06-13.
 */
public class SSLListenerTest1 {

    @Test
    public void test1() throws IOException, InterruptedException {
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket("www.baidu.com", 443);
        sslSocket.setEnabledCipherSuites(sslSocketFactory.getSupportedCipherSuites());
        sslSocket.addHandshakeCompletedListener((HandshakeCompletedEvent handshakeCompletedEvent) -> {
//            System.out.println(handshakeCompletedEvent.getSession());
            System.out.println(handshakeCompletedEvent.getCipherSuite());
            SSLSocket socket = handshakeCompletedEvent.getSocket();
            try (InputStream in = socket.getInputStream()) {
                StringBuilder response = new StringBuilder();
                int c;
                while ((c = in.read()) != -1) {
                    response.append((char) c);
                }
                System.out.println(response);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        System.out.println("异步");
        TimeUnit.SECONDS.sleep(50);
    }

}

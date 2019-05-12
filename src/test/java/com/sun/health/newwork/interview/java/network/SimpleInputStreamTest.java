package com.sun.health.newwork.interview.java.network;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class SimpleInputStreamTest {

    @Test
    public void test1() {
        byte[] data = new byte[10];
        try (InputStream inputStream = new ByteInputStream()) {
            for (int i = 0; i < 10; i++) {
                int read = inputStream.read();
                if (read == -1)
                    break;
                data[i] = (byte) read;
            }
            System.out.println(new String(data, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

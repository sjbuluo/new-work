package com.sun.health.newwork.interview.java.network.url_connection;

import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by 华硕 on 2019-05-24.
 */
public class SimpleURLConnectionTest {
    
    @Test
    public void test1() {
        try {
            URL url = new URL("http://www.baidu.com");
            URLConnection urlConnection = url.openConnection();
            System.out.println("Content-type: " + urlConnection.getContentType());
            System.out.println("Content-length: :"  + urlConnection.getContentLength());
            System.out.println("Content-encoding: " + urlConnection.getContentEncoding());
            System.out.println("Date: " + urlConnection.getDate());
            System.out.println("Expiration: " + urlConnection.getExpiration());
            System.out.println("LastModified: " + urlConnection.getLastModified());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOutput() {
        try {
            URL url = new URL("https://www.baidu.com/s");
            URLConnection urlConnection = url.openConnection();
            urlConnection.setDoOutput(true);
            try (OutputStream outputStream = urlConnection.getOutputStream(); OutputStreamWriter osw = new OutputStreamWriter(outputStream); BufferedWriter bufferedWriter = new BufferedWriter(osw)) {
                osw.write("wd=Hello World");
                osw.flush();
            }
            try (InputStream in = urlConnection.getInputStream(); InputStreamReader inr = new InputStreamReader(in); BufferedReader br = new BufferedReader(inr); FileWriter fileWriter = new FileWriter("baidu_post_hello_world.html")) {
                String line = null;
                while((line = br.readLine()) != null) {
                    fileWriter.write(line + "\n");
                }
            }
            System.out.println("完成");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRequestHead() {
        testReqeustMethod("HEAD");
    }

    @Test
    public void testRequestDelete() {
        testReqeustMethod("DELETE");
    }

    @Test
    public void testRequestOptions() {
        testReqeustMethod("OPTIONS");
    }

    private void testReqeustMethod(String requestMethod) {
        try {
            URL url = new URL("https://www.baidu.com");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(requestMethod);
            for (Map.Entry<String, List<String>> stringListEntry : connection.getHeaderFields().entrySet()) {
                System.out.println(stringListEntry.getKey() + " : " + stringListEntry.getValue());
            }
            try (InputStream in = connection.getInputStream(); InputStreamReader inr = new InputStreamReader(in); BufferedReader br = new BufferedReader(inr)) {
                String line = null;
                while((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}

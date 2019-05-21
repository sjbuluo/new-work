package com.sun.health.newwork.interview.java.network;

import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by 华硕 on 2019-05-21.
 */
public class SimpleURLTest {

    @Test
    public void test1() {
        testProtocol("http://");
        testProtocol("https://");
        testProtocol("ftp://");
        testProtocol("mailto://");
        testProtocol("telnet://");
        testProtocol("file://");
        testProtocol("gopher://");
        testProtocol("ldap://");
        testProtocol("jar:http://");
        testProtocol("nfs://");
        testProtocol("jdbc:mysql://");
        testProtocol("rmi://");
        testProtocol("doc:/");
        testProtocol("netdoc:/");
        testProtocol("systemresource://");
        testProtocol("verbatim:http://");
    }

    private void testProtocol(String url) {
        try {
            URL result = new URL(url);
        } catch (MalformedURLException e) {
            System.out.println("不支持[ " + url + " ]的协议");
        }
    }

    @Test
    public void test2() {
        try {
            URL http = new URL("http", "www.163.com", "/hello/world.html");
            System.out.println(http);
            URL relativeHttp = new URL(http, "1.html");
            System.out.println(relativeHttp);
            URL relativeHttp2 = new URL(http, "/2.html");
            System.out.println(relativeHttp2);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() throws MalformedURLException {
        File file = new File("PHP.txt");
        System.out.println(file.toURL());
        System.out.println(file.toURI());
    }

    @Test
    public void test4() {
        try (FileWriter fw = new FileWriter("baidu.html")){
            URL url = new URL("https://www.baidu.com");
            try (InputStream inputStream = url.openStream(); InputStreamReader isr = new InputStreamReader(inputStream); BufferedReader br = new BufferedReader(isr)) {
                String line;
                while((line = br.readLine()) != null) {
                    System.out.println(line);
                    fw.write(line);
                }
                fw.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test5() {
        try {
            URL url = new URL("http://www.biadu.com");
            URLConnection urlConnection = url.openConnection();
            System.out.println(url.getContent());
            for (Map.Entry<String, List<String>> stringListEntry : urlConnection.getHeaderFields().entrySet()) {
                System.out.println(stringListEntry.getKey() + " : " + stringListEntry.getValue());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test6() {
        File file = new File("PHP.txt");
        try {
//            URL url = file.toURL();
            URL url = new URL("https://www.baidu.com");
            System.out.println(url.getProtocol());
            System.out.println(url.getUserInfo());
            System.out.println(url.getAuthority());
            System.out.println(url.getHost());
            System.out.println(url.getPort());
            System.out.println(url.getDefaultPort());
            System.out.println(url.getPath());
            System.out.println(url.getFile());
            System.out.println(url.getQuery());
            System.out.println(url.getRef());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}

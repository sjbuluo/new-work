package com.sun.health.newwork.interview.java.network.uri_url;

import org.junit.Test;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 华硕 on 2019-05-23.
 */
public class GetRequestUrl {

    public void sendGetRequest(String urlPrev, Map<String, Object> params) {
        QueryString queryString = new QueryString();
        for (Map.Entry<String, Object> stringObjectEntry : params.entrySet()) {
            queryString.add(stringObjectEntry.getKey(), stringObjectEntry.getValue().toString());
        }
        try {
            URL url = new URL(urlPrev + queryString.getQUery());
            URLConnection urlConnection = url.openConnection();
            try (InputStream in = urlConnection.getInputStream(); InputStreamReader isr = new InputStreamReader(in); BufferedReader br = new BufferedReader(isr)) {
                String line = null;
                while((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() {
        Map<String, Object> params = new HashMap<>();
        params.put("wd", "Hello World");
        sendGetRequest("http://www.baidu.com/s?",params);
    }
}

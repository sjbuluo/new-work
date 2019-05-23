package com.sun.health.newwork.interview.java.network.http;

import org.junit.Test;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Map;

/**
 * Created by 华硕 on 2019-05-23.
 */
public class CustomCookiePolicy implements CookiePolicy {
    @Override
    public boolean shouldAccept(URI uri, HttpCookie cookie) {
        if(uri.getAuthority().toLowerCase().indexOf("qq") != -1) {
            return true;
        }
        return false;
    }

    @Test
    public void test1() {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(new CustomCookiePolicy());
        CookieHandler.setDefault(cookieManager);
        try {
            URL url = new URL("http://www.163.com");
            URLConnection urlConnection = url.openConnection();
            for (Map.Entry<String, List<String>> stringListEntry : urlConnection.getHeaderFields().entrySet()) {
                System.out.println(stringListEntry.getKey() + " : " + stringListEntry.getValue());
            }
            System.out.println("---------------------------------------------");
            url = new URL("http://www.qq.com");
            urlConnection = url.openConnection();
            for (Map.Entry<String, List<String>> stringListEntry : urlConnection.getHeaderFields().entrySet()) {
                System.out.println(stringListEntry.getKey() + " : " + stringListEntry.getValue());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
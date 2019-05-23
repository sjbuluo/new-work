package com.sun.health.newwork.interview.java.network.uri_url;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by 华硕 on 2019-05-23.
 */
public class QueryString {

    private StringBuilder query = new StringBuilder();

    public QueryString() {
    }

    public synchronized void add(String name, String value) {
        query.append("&");
        encode(name, value);
    }

    private synchronized void encode(String name, String value) {
        try {
            query.append(URLEncoder.encode(name, "UTF-8"));
            query.append("=");
            query.append(URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Broken VM does not support UTF-8");
        }
    }

    public synchronized String getQUery() {
        return query.toString();
    }

    @Override
    public String toString() {
        return getQUery();
    }
}

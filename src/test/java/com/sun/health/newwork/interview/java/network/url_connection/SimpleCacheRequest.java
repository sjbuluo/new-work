package com.sun.health.newwork.interview.java.network.url_connection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CacheRequest;

/**
 * Created by 华硕 on 2019-05-24.
 */
public class SimpleCacheRequest extends CacheRequest {

    private ByteArrayOutputStream out =  new ByteArrayOutputStream();

    @Override
    public OutputStream getBody() throws IOException {
        return out;
    }

    @Override
    public void abort() {
        out.reset();
    }

    public byte[] getData() {
        if (out.size() == 0)
            return null;
        return out.toByteArray();
    }
}

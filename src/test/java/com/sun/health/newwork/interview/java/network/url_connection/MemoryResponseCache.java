package com.sun.health.newwork.interview.java.network.url_connection;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 华硕 on 2019-05-24.
 */
public class MemoryResponseCache extends ResponseCache {

    private final Map<URI, SimpleCacheResponse> responses = new ConcurrentHashMap<>();

    private final int maxEntries;

    public MemoryResponseCache() {
        this(100);
    }

    public MemoryResponseCache(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    @Override
    public CacheResponse get(URI uri, String rqstMethod, Map<String, List<String>> rqstHeaders) throws IOException {
        SimpleCacheResponse simpleCacheResponse = null;
        if ("GET".equals(rqstMethod))
            simpleCacheResponse = responses.get(uri);
        if (simpleCacheResponse != null && !simpleCacheResponse.isExpired()) {
            return responses.remove(uri);
        }
        return null;
    }

    @Override
    public CacheRequest put(URI uri, URLConnection conn) throws IOException {
        if (responses.size() >= maxEntries)
            return null;
        CacheControl cacheControl = new CacheControl(conn.getHeaderField("Cache-Control"));
        if (cacheControl.isNoStore()) {
            return null;
        } else if (!conn.getHeaderField(0).startsWith("GET")) {
            return null;
        }
        SimpleCacheRequest simpleCacheRequest = new SimpleCacheRequest();
        SimpleCacheResponse simpleCacheResponse = new SimpleCacheResponse(simpleCacheRequest, conn, cacheControl);
        responses.put(uri, simpleCacheResponse);
        return simpleCacheRequest;
    }
}

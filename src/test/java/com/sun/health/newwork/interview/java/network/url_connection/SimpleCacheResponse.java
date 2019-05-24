package com.sun.health.newwork.interview.java.network.url_connection;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CacheResponse;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by 华硕 on 2019-05-24.
 */
public class SimpleCacheResponse extends CacheResponse {

    private final Map<String, List<String>> headers;

    private final SimpleCacheRequest request;

    private final Date expires;

    private final CacheControl control;

    public SimpleCacheResponse(SimpleCacheRequest request, URLConnection connection, CacheControl control) {
        this.headers = Collections.unmodifiableMap(connection.getHeaderFields());
        this.request = request;
        this.expires = new Date(connection.getExpiration());
        this.control = control;
    }

    @Override
    public Map<String, List<String>> getHeaders() throws IOException {
        return headers;
    }

    @Override
    public InputStream getBody() throws IOException {
        return new ByteArrayInputStream(request.getData());
    }

    public CacheControl getControl() {
        return control;
    }

    public boolean isExpired() {
        Date now = new Date();
        if (control.getMaxAge().before(now))
            return true;
        else if (expires != null && control.getMaxAge() != null)
            return expires.before(now);
        return false;
    }

}

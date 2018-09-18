package executor.impl;

import executor.HttpRequestKeeper;
import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;

import java.io.InputStream;
import java.util.List;

public class HttpRequestKeeperImpl implements HttpRequestKeeper {
    private HttpClientContext clientContext;
    private CookieStore cookieStore;
    private List<Cookie> cookies;
    private List<Header> headers;
    private CloseableHttpResponse httpResponse;
    private int statusCode;
    private String url;
    private boolean executed = false;

    public HttpRequestKeeperImpl(String url) {
        this.url = url;
    }

    @Override
    public CloseableHttpResponse getHttpResponse(String url) {

        return null;
    }

    @Override
    public HttpClientContext getHttpClientContext() {
        return null;
    }

    @Override
    public CookieStore getCookieStore() {
        return null;
    }

    @Override
    public List<Cookie> getCookies() {
        return null;
    }

    @Override
    public List<Header> getHeaders() {
        return null;
    }

    @Override
    public InputStream getContentAsStream() {
        return null;
    }

    @Override
    public String getContentAsString() {
        return null;
    }

    @Override
    public String getUrl() {
        return url;
    }
}

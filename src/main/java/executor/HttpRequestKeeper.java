package executor;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;

import java.io.InputStream;
import java.util.List;

public interface HttpRequestKeeper {

    HttpClientContext getHttpClientContext();

    CookieStore getCookieStore();

    List<Cookie> getCookies();

    List<Header> getHeaders();

    CloseableHttpResponse getHttpResponse(String url);

    InputStream getContentAsStream();

    String getContentAsString();

    String getUrl();

}

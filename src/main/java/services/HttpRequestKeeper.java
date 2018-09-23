package services;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class HttpRequestKeeper {
    private HttpClientContext clientContext;
    private CookieStore cookieStore;
    private List<Cookie> cookies;
    private Map<String, String> requestHeaders;
    private Header[] responseHeaders;
    private CloseableHttpResponse httpResponse = null;
    private String page = null;
    private Document doc = null;
    private int statusCode;
    private StatusLine statusLine;
    private String url;
    private boolean executed = false;
    private boolean error = false;

    public void sendGet(String url, Map<String, String> headers) {
        if (error || !executed) {
            requestHeaders = headers;
            clientContext = HttpClientContext.create();
            try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD).build()).build()) {
                HttpGet httpGet = new HttpGet(url);
                for (String key : headers.keySet()) {
                    httpGet.addHeader(key, headers.get(key));
                }
                httpResponse = httpClient.execute(httpGet, clientContext);
                // TODO: 22.09.2018 сделать отдельный метод для установки полей класса
                executed = true;
                error = false;
                responseHeaders = httpResponse.getAllHeaders();
                statusLine = httpResponse.getStatusLine();
                statusCode = httpResponse.getStatusLine().getStatusCode();
                cookieStore = clientContext.getCookieStore();
                cookies = cookieStore.getCookies();
                page = formPage(httpResponse);
            } catch (IOException ioe) {
                error = true;
                System.err.println(ioe.getMessage());
            }
        }
    }

    public void sendPost(String utl, Map<String, String> headers, List<NameValuePair> postParams) {
        if (error || !executed) {
            requestHeaders = headers;
            clientContext = HttpClientContext.create();
            try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(RequestConfig.custom()
                    .setCookieSpec(CookieSpecs.STANDARD).build()).build()) {
                HttpPost httpPost = new HttpPost(url);
                for (String key : headers.keySet()) {
                    httpPost.addHeader(key, headers.get(key));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(postParams));
                httpResponse = httpClient.execute(httpPost, clientContext);
                // TODO: 22.09.2018 сделать отдельный метод для установки полей класса
                executed = true;
                error = false;
                responseHeaders = httpResponse.getAllHeaders();
                statusLine = httpResponse.getStatusLine();
                statusCode = httpResponse.getStatusLine().getStatusCode();
                cookieStore = clientContext.getCookieStore();
                cookies = cookieStore.getCookies();
                page = formPage(httpResponse);
                parsePage(page);
            } catch (IOException ioe) {
                error = true;
                System.err.println(ioe.getMessage());
            }
        }
    }

    public String getPage() {
        return page;
    }

    private Document parsePage(String page) {
        // TODO: 23.09.2018 правильно ли так делать???
        if (page == null) {
            throw new NullPointerException("The Page is null!");
        }
        if (doc == null) {
            doc = Jsoup.parse(page);
        }
        return doc;
    }

    public Elements getInputFieldsFromForm(String formId) {
        Element form = doc.getElementById(formId);
        List<Element> inputs = new ArrayList<>();

        for (Element e: form.getAllElements()) {
            if (e.tagName().equals("input")) {
                inputs.add(e);
            } else {
                if (!e.children().isEmpty()) {
                    for (Element ce : e.children()) {
                        // TODO: 23.09.2018 продумать, разбить на несколько методов!!!
                    }
                }
            }
        }
        for (Element e : doc.select(String.format("form#%s > *", formId))) {

        }
        //Elements forms = doc.select(String.format("%s", formId));
        return null;
    }

    private String formPage(CloseableHttpResponse httpResponse) {
        String inputLine;
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(httpResponse.getEntity().getContent()))) {
            while ((inputLine = reader.readLine()) != null) {
                builder.append(inputLine);
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
            return null;
        }
        return builder.toString();
    }

    public Document getDoc() {
        return doc;
    }

    public CloseableHttpResponse getHttpResponse() {
        return httpResponse;
    }

    public List<Header> getResponseHeaders() {
        return Arrays.asList(responseHeaders);
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public HttpClientContext getHttpClientContext() {
        return clientContext;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public InputStream getPageAsStream() throws IOException {
        return httpResponse.getEntity().getContent();
    }

    public int getStatusCode() {
        return statusCode;
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public boolean isError() {
        return error;
    }

    public boolean isExecuted() {
        return executed;
    }

    public String getUrl() {
        return url;
    }

}

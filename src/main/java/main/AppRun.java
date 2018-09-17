package main;

import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Simplest program that tries to log into vk.com via http using Jsoup and Apache HttpClient
 *
 * @author Denis Tyurin
 */
public class AppRun {
    private static final String MAIN_URL = "https://vk.com";
    private static final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws IOException {
        sendGet(MAIN_URL);
    }

    private static void sendGet(String url) throws IOException {
        HttpClientContext context = HttpClientContext.create();
        //CloseableHttpClient httpClient = HttpClients.createDefault(); // каждая куки вызывает warning, что она имеет неправильный формат
        //HttpClient client = HttpClientBuilder.create().build(); // результат тот же, что и при вызове метода выше
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD).build()) //The RFC 6265 compliant policy (interoprability profile).
                .build();

        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("User-Agent", USER_AGENT);

        CloseableHttpResponse httpResponse = httpClient.execute(httpGet, context);

        CookieStore cookieStore = context.getCookieStore();
        List<Cookie> cookieList = cookieStore.getCookies();

        System.out.println("GET Response Status:: " + httpResponse.getStatusLine().getStatusCode());

        Header[] headers = httpResponse.getAllHeaders();
        for (Header header : headers) {
            System.out.println("Key [ " + header.getName() + "], Value[ " + header.getValue() + " ]");
        }

        System.out.println("\nRead Specific Header Value");
        System.out.println("Date Header:- " + httpResponse.getFirstHeader("Date").getValue());

        cookieList.forEach(System.out::println);

        //        int inByte; // проверить этот код, он может работать неправильно!!
//        while ((inByte = is.read()) != -1) {
//            fos.write(inByte);
//        }


        BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();

        System.out.println(response.toString());
        httpClient.close();
    }
}

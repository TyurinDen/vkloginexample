package main;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import services.HttpRequestKeeper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simplest program that tries to log into vk.com via http using Jsoup and Apache HttpClient
 *
 * @author Denis Tyurin
 */
public class AppRun {
    private static final String MAIN_URL = "https://vk.com";
    private static final String USER_AGENT = "Mozilla/5.0";

    public static void main(String[] args) throws IOException {
//        File input = new File("src/main/resources/loginpageatall.html");
//        Document doc = Jsoup.parse(input, "windows-1251", "https://vk.com");


//        System.out.println("1- " + form);
//        for (Element e : doc.select("form#quick_login_form > *")) {
//            System.out.println(e);
//            if (!e.children().isEmpty()) {
//                for (Element c : e.children()) {
//                    System.out.println("child: " + c);
//                }
//            }
//        }
        HttpRequestKeeper mainReq = new HttpRequestKeeper();
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", USER_AGENT);
        mainReq.sendGet(MAIN_URL, headers);
        System.out.println(mainReq.getStatusCode());
        System.out.println(mainReq.getStatusLine().getReasonPhrase());
        mainReq.savePageToFile("src/files/req.html", mainReq.getPageAsString());

    }

    private static void sendGet(String url) throws IOException {
        //CloseableHttpClient httpClient = HttpClients.createDefault(); // каждая куки вызывает warning, что она имеет неправильный формат
        //HttpClient client = HttpClientBuilder.create().build(); // результат тот же, что и при вызове метода выше
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setCookieSpec(CookieSpecs.STANDARD).build()) //The RFC 6265 compliant policy (interoprability profile).
                .build();
    }
}

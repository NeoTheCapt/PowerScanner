package BrianW.AKA.BigChan.Tools;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.net.*;
import java.util.*;

public class RequestHelper {
    public static void doRequestViaProxy(URL url, String method, List<String> headers, byte[] body, HttpHost proxy, String user, String pass) throws Exception {
        String protocol = url.getProtocol();
        Credentials credentials = new UsernamePasswordCredentials(user, pass);
        AuthScope authScope = new AuthScope(proxy.getHostName(), proxy.getPort());
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(authScope, credentials);

        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial((chain, authType) -> true).build();
        SSLConnectionSocketFactory sslConnectionSocketFactory =
                new SSLConnectionSocketFactory(sslContext, new String[]
                        {"SSLv3", "TLSv1","TLSv1.1", "TLSv1.2" }, null,
                        NoopHostnameVerifier.INSTANCE);

        CloseableHttpClient httpClient = HttpClients
                .custom()
//                .setSSLSocketFactory(new SSLConnectionSocketFactory(SSLContexts.custom()
//                                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
//                                .build()
//                        )
//                )
                .setSSLSocketFactory(sslConnectionSocketFactory)
//                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
//                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .setProxy(proxy)
                .setDefaultCredentialsProvider(credsProvider)
                .build();
        RequestBuilder requestBuilder = RequestBuilder.get();
        switch (method.toLowerCase()) {
            case "get":
                requestBuilder = RequestBuilder.get();
                break;
            case "post":
                requestBuilder = RequestBuilder.post();
                break;
            case "put":
                requestBuilder = RequestBuilder.put();
                break;
            case "delete":
                requestBuilder = RequestBuilder.delete();
                break;
            case "patch":
                requestBuilder = RequestBuilder.patch();
                break;
            case "options":
                requestBuilder = RequestBuilder.options();
                break;
            case "head":
                requestBuilder = RequestBuilder.head();
                break;
        }
        setHeader(requestBuilder, headers);
        if (body.length > 0){
            ByteArrayEntity bodyEntity = new ByteArrayEntity(body);
            requestBuilder.setEntity(bodyEntity);
        }
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(1000)
                .setSocketTimeout(5000).build();
        HttpUriRequest request = requestBuilder
                .setUri(url.toURI())
                .setConfig(requestConfig)
                .build();
        CloseableHttpResponse response = httpClient.execute(request);
        httpClient.close();
        response.close();
    }

    public static void main(String[] args) throws Exception {
        URL url = new URL("https://xs3c.co/ip.php");
        ArrayList<String> header = new ArrayList<>();
        header.add("connection: keep-alive");
        header.add("content-type: application/json");
        String body = "{\"username\": \"admin\", \"password\": \"123qwe!@#QWE\"}";
        HttpHost proxy = new HttpHost("192.168.1.219", 833);
//        HttpHost proxy = new HttpHost("192.168.124.128", 8081);
        String method = "post";
        doRequestViaProxy(url, method, header, body.getBytes(), proxy, "usual", "asdqwe123");

    }
    private static void setHeader(RequestBuilder requestBuilder, List<String> headers) {
        headers.forEach((s) -> {
            if (s.contains(": ")) {
                String[] split = s.split(": ");
                requestBuilder.addHeader(split[0], split[1]);
            }
        });
    }
}

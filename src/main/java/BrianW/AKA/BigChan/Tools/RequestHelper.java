package BrianW.AKA.BigChan.Tools;

import javax.net.ssl.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class RequestHelper {
    public static byte[] doRequestViaProxy(URL url, List<String> headers, byte[] body, Proxy proxy) throws Exception {
        String protocol = url.getProtocol();
        //  String path = url.getPath();
        //  String urlStr = protocol+"://"+proxyIP+":"+proxyPort+path;
        URLConnection httpURLConnection;
        if (protocol.toLowerCase().equals("https")) {

            httpURLConnection = new TestPersistentConnection().request(url, false, proxy);
        } else {
            httpURLConnection = url.openConnection(proxy);
        }
        setHeader(httpURLConnection, headers);
        if (null == body) {
            //get代理请求
            return sendGet(httpURLConnection);
        } else {
            //post代理请求
            return sendPost(httpURLConnection, new String(body));
        }

    }

    /**
     * 发送GET请求
     *
     * @return
     */
    public static byte[] sendGet(URLConnection connection) {
        byte[] result = null;

        StringBuffer sb = new StringBuffer();
        BufferedReader in = null;
        try {
            // 建立实际的连接
            connection.connect();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString().getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭输入流
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 发送post请求(返回json)
     *
     * @param body
     * @return
     */
    public static byte[] sendPost(URLConnection connection, String body) {
        byte[] result = null;
        PrintWriter out = null;
        BufferedReader in = null;
        String res = "";
        try {
            // 发送POST请求必须设置如下两行
            connection.setDoOutput(true);
            connection.setDoInput(true);
            // 获取URLConnection对象对应的输出流（设置请求编码为UTF-8）
            out = new PrintWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
            // 发送请求参数
            out.print(body);
            // flush输出流的缓冲
            out.flush();
            // 获取请求返回数据（设置返回数据编码为UTF-8）
            in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = in.readLine()) != null) {
                res += line;
            }
            result = res.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    private static void setHeader(URLConnection conn, List<String> headers) {
        headers.forEach((s) -> {
            if (s.contains(": ")){
                String[] split = s.split(": ");
                conn.setRequestProperty(split[0], split[1]);
            }
        });
    }

    private static SSLSocketFactory createSSLSocketFactory() throws Exception {
        File crtFile = new File("server.crt");
        Certificate certificate = CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(crtFile));

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("server", certificate);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        return sslContext.getSocketFactory();
    }

    public static void main(String[] args) throws Exception {
        /*URL url = new URL("https://220.181.38.150");
        ArrayList<String> header = new ArrayList<>();

        header.add("connection: keep-alive");
        byte[] bytes = doRequestViaProxy(url, header, null, "220.181.38.150", 443);
        String s = new String(bytes);
        System.out.println(s);*/

        //post
        URL url = new URL("https://xs3c.co/ip.php");
        ArrayList<String> header = new ArrayList<>();
        header.add("connection: keep-alive");
        header.add("content-type: application/json");
        String body = "{\"username\": \"admin\", \"password\": \"123qwe!@#QWE\"}";
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8080));
        byte[] bytes = doRequestViaProxy(url, header, body.getBytes(), proxy);
        String s = new String(bytes);
        System.out.println(s);

    }

    static class TestPersistentConnection {
        private static SSLSocketFactory sslSocketFactory = null;

        /**
         * Use the VM argument <code>-Djavax.net.debug=ssl</code> for SSL specific debugging;
         * the SSL handshake will appear a single time when connections are re-used, and multiple
         * times when they are not.
         * <p>
         * Use the VM <code>-Djavax.net.debug=all</code> for all network related debugging, but
         * note that it is verbose.
         *
         * @throws Exception
         */
       /* public static void main(String[] args) throws Exception
        {

            //URL url = new URL("https://google.com/");
            URL url = new URL("https://220.181.38.150:443/");

            // Disable first
            request(url, false);

            // Enable; verifies our previous disable isn't still in effect.
            request(url, true);
        }*/
        public URLConnection request(URL url, boolean enableCertCheck, Proxy proxy) throws Exception {
            BufferedReader reader = null;
            // Repeat several times to check persistence.
            System.out.println("Cert checking=[" + (enableCertCheck ? "enabled" : "disabled") + "]");
            //   for (int i = 0; i < 5; ++i) {
            try {
                HttpsURLConnection httpConnection = (HttpsURLConnection) url.openConnection(proxy);
                // Normally, instanceof would also be used to check the type.
                if (!enableCertCheck) {
                    setAcceptAllVerifier(httpConnection);
                }
                return httpConnection;
            } catch (IOException ex) {
                System.out.println(ex);

            }
            //    }
            return null;
        }

        /**
         * Overrides the SSL TrustManager and HostnameVerifier to allow
         * all certs and hostnames.
         * WARNING: This should only be used for testing, or in a "safe" (i.e. firewalled)
         * environment.
         *
         * @throws NoSuchAlgorithmException
         * @throws KeyManagementException
         */
        protected void setAcceptAllVerifier(HttpsURLConnection connection) throws NoSuchAlgorithmException, KeyManagementException {

            // Create the socket factory.
            // Reusing the same socket factory allows sockets to be
            // reused, supporting persistent connections.
            if (null == sslSocketFactory) {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, ALL_TRUSTING_TRUST_MANAGER, new java.security.SecureRandom());
                sslSocketFactory = sc.getSocketFactory();
            }

            connection.setSSLSocketFactory(sslSocketFactory);

            // Since we may be using a cert with a different name, we need to ignore
            // the hostname as well.
            connection.setHostnameVerifier(ALL_TRUSTING_HOSTNAME_VERIFIER);
        }

        private final TrustManager[] ALL_TRUSTING_TRUST_MANAGER = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        private final HostnameVerifier ALL_TRUSTING_HOSTNAME_VERIFIER = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

    }
}

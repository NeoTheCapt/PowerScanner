package BrianW.AKA.BigChan.Tools;

import burp.IBurpExtenderCallbacks;
import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class utils {
    /**
     * @param str     原字符串
     * @param sToFind 需要查找的字符串
     * @return 返回在原字符串中sToFind出现的次数
     */
    public static int countStr(String str, String sToFind) {
        int num = 0;
        while (str.contains(sToFind)) {
            str = str.substring(str.indexOf(sToFind) + sToFind.length());
            num++;
        }
        return num;
    }

    /**
     * convert byte[] to HexString
     *
     * @param bArray
     * @param length
     * @return
     */
    public static String bytesToHexString(byte[] bArray, int length) {
        StringBuffer sb = new StringBuffer(length);
        String sTemp;
        for (int i = 0; i < length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static String getStackMsg(Exception e) {

        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackArray = e.getStackTrace();
        sb.append(e.getMessage() + "\n");
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString() + "\n");
        }
        return sb.toString();
    }

    public static String getStackMsg(Throwable e) {

        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackArray = e.getStackTrace();
        sb.append(e.getMessage() + "\n");
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString() + "\n");
        }
        return sb.toString();
    }

    public static String IpGen() {
        Random r = new Random();
        return r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256) + "." + r.nextInt(256);
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static int getRandomInt(int max, int min) {
        Random random = new Random();
        int randomWintNextIntWithinARange = random.nextInt(max - min) + min;
        return randomWintNextIntWithinARange;
    }

    private static final Gson gson = new Gson();

    public static boolean isJson(String jsonInString) {
        try {
            gson.fromJson(jsonInString, Object.class);
            return true;
        } catch (com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

    public static String StrToUnicode(String str) {
        StringBuffer outHexStrBuf = new StringBuffer();
        for (char c : str.toCharArray()) {
            outHexStrBuf.append("\\\\u");
            String hexStr = Integer.toHexString(c);
            for (int i = 0; i < (4 - hexStr.length()); i++) outHexStrBuf.append("0");
            outHexStrBuf.append(hexStr);
        }
        return outHexStrBuf.toString();
    }

    public static String encodeJson2Unicode(String jsonStr) {
        String regex = "\".*?\"";
        StringBuffer sb = new StringBuffer();
        String replacementText = "";
        String matchedText = "";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(jsonStr);

        while (m.find()) {
            matchedText = m.group();
            if (matchedText.contains("\\u")) {
                continue;
            }
            replacementText = "\"" + StrToUnicode(matchedText.substring(1, matchedText.length() - 1)) + "\"";
            m.appendReplacement(sb, replacementText);
        }

        m.appendTail(sb);
        return sb.toString();
    }

    public static boolean rangeInDefined(int current, int min, int max) {
        return Math.max(min, current) == Math.min(current, max);
    }

    public static String getBaseUrl(URL url) {
        return url.getProtocol() + "://" + url.getAuthority();
    }

    public static String getIconUrlString(URL url) throws KeyManagementException, NoSuchAlgorithmException {
        //System.out.println("run into getIconUrlString");
        String iconUrl = "/favicon.ico";// 保证从域名根路径搜索
        if (hasRootIcon(getBaseUrl(url) + iconUrl)) {
            System.out.println("run into hasRootIcon");
            return iconUrl;
        }
        Connection con2 = Jsoup.connect(getBaseUrl(url));
        Document doc = null;
        try {
            doc = con2.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Element e;
        iconUrl = "";
        if (!doc.head().select("link[href~=.*\\.(ico|png)]").isEmpty()){
            //System.out.println("run into 1st regx");
            e = doc.head().select("link[href~=.*\\.(ico|png)]").first();
            iconUrl = e.attr("href");
        }
        if (iconUrl.equals("") && !doc.head().select("meta[itemprop=image]").isEmpty()){
            //System.out.println("run into 2rd regx");
            e = doc.head().select("meta[itemprop=image]").first();
            iconUrl = e.attr("itemprop");
        }
        if (!iconUrl.startsWith("/")){
            iconUrl = "/" + iconUrl;
        }
        return iconUrl;
    }

    public static String getWebsiteTitle(URL url) {
        Connection con2 = Jsoup.connect(getBaseUrl(url));
        Document doc;
        try {
            doc = con2.get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return doc.title();
    }

    private static boolean hasRootIcon(String urlString) throws NoSuchAlgorithmException, KeyManagementException {
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(getAllHostsValid());
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(urlString).openConnection();
            ;
            connection.connect();
            return HttpURLConnection.HTTP_OK == connection.getResponseCode() && connection.getContentLength() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    public static byte[] httpGet(String urlString) throws NoSuchAlgorithmException, KeyManagementException {

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(getAllHostsValid());
        HttpURLConnection connection = null;
        byte[] rst = new byte[]{};
        try {
            connection = (HttpURLConnection) new URL(urlString).openConnection();
            ;
            connection.setInstanceFollowRedirects(true);
            connection.connect();
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode() && connection.getContentLength() > 0) {
                InputStream response = connection.getInputStream();
                rst = response.readAllBytes();
            }
            return rst;
        } catch (Exception e) {
            e.printStackTrace();
            return rst;
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }

    public static String iconb64Hash(String iconb64) {
        StringBuilder newIconB64 = new StringBuilder();
        for (int i = 0; i < iconb64.length(); i++) {
            newIconB64.append(iconb64.charAt(i));
            if ((i + 1) % 76 == 0) {
                newIconB64.append("\n");
            }
        }
        if (!(iconb64.length() % 76 == 0)){
            newIconB64.append("\n");
        }
        int iconHash = Hashing.murmur3_32().newHasher().putString(newIconB64, StandardCharsets.UTF_8).hash().asInt();
        return String.valueOf(iconHash);
    }
    private static HostnameVerifier getAllHostsValid() throws NoSuchAlgorithmException, KeyManagementException {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        return allHostsValid;
    }


}


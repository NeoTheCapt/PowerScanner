package BrianW.AKA.BigChan.Tools;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class domainTool {

    private final static Set<String> PublicSuffixSet = new HashSet<String>(
            Arrays.asList(new String(
                    "com|org|net|gov|edu|co|tv|mobi|info|asia|xxx|onion|cn|com.cn|edu.cn|gov.cn|net.cn|org.cn|jp|kr|tw|com.hk|hk|com.hk|org.hk|se|com.se|org.se")
                    .split("\\|")));

    private static Pattern IP_PATTERN = Pattern.compile("(\\d{1,3}\\.){3}(\\d{1,3})");

    /**
     * 获取url的顶级域名
     * @param url
     * @return
     */
    public static String getDomainName(URL url) {
        String host = url.getHost();
        if (host.endsWith(".")){
            host = host.substring(0, host.length() - 1);
        }
        if (IP_PATTERN.matcher(host).matches()){
            return host;
        }

        int index = 0;
        String candidate = host;
        for (; index >= 0;) {
            index = candidate.indexOf('.');
            String subCandidate = candidate.substring(index + 1);
            if (PublicSuffixSet.contains(subCandidate)) {
                return candidate;
            }
            candidate = subCandidate;
        }
        return candidate;
    }

    /**
     * 获取url的顶级域名
     * @param url
     * @return
     * @throws MalformedURLException
     */
    public static String getDomainName(String url) throws MalformedURLException {
        return getDomainName(new URL(url));
    }

    /**
     * 判断两个url顶级域名是否相等
     * @param url1
     * @param url2
     * @return
     */
    public static boolean isSameDomainName(URL url1, URL url2) {
        return getDomainName(url1).equalsIgnoreCase(getDomainName(url2));
    }

    /**
     * 判断两个url顶级域名是否相等
     * @param url1
     * @param url2
     * @return
     * @throws MalformedURLException
     */
    public static boolean isSameDomainName(String url1, String url2)
            throws MalformedURLException {
        return isSameDomainName(new URL(url1), new URL(url2));
    }
}
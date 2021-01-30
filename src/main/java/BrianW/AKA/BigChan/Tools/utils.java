package BrianW.AKA.BigChan.Tools;

import com.google.gson.Gson;

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
        for (int i = 0; i < stackArray.length; i++) {
            StackTraceElement element = stackArray[i];
            sb.append(element.toString() + "\n");
        }
        return sb.toString();
    }

    public static String getStackMsg(Throwable e) {

        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stackArray = e.getStackTrace();
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
}


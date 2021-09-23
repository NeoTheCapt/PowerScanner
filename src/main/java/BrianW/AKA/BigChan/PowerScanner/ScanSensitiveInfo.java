package BrianW.AKA.BigChan.PowerScanner;

import burp.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanSensitiveInfo extends Reporter {
    protected IBurpExtenderCallbacks callbacks;
    protected IExtensionHelpers helpers;
    protected String[] sensitiveWords = new String[]{
            "博彩", "赌博", "太阳城", "bet365",
    };

    public ScanSensitiveInfo(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
        super(callbacks, helpers);
        this.callbacks = callbacks;
        this.helpers = helpers;
    }

    public List<IScanIssue> doScanSensitiveInfo(IHttpRequestResponse baseRequestResponse) {

        List<IScanIssue> issues = new ArrayList<>();
        IResponseInfo responseInfo  = helpers.analyzeResponse(baseRequestResponse.getResponse());
        String mimeType = responseInfo.getStatedMimeType();
        String inferredMimeType = responseInfo.getInferredMimeType();
        callbacks.printOutput("doScanSensitiveInfo： mimeType=" + mimeType);
        callbacks.printOutput("doScanSensitiveInfo： inferredMimeType=" + inferredMimeType);
        String content = callbacks.getHelpers().bytesToString(baseRequestResponse.getResponse());
        if (mimeType.contains("HTML")){
            callbacks.printOutput("doScanSensitiveInfo: " + mimeType);
            Matcher matchID = searchID(content);
            if (matchID.find()){
                callbacks.printOutput("doScanSensitiveInfo: matchID" );
                issues.add(
                        reporter(
                                "Sensitive Information found (ID)",
                                String.format("Found Sensitive Information: IDs <br>" +
                                                "IDs: %s <br>",
                                        matchToString(matchID)
                                ),
                                "Medium",
                                "Certain"
                        )
                );
            }
        }

        return issues;
    }

    //敏感字
    private String searchSensitiveWords(String param) {
        for (String word : sensitiveWords) {
            if (param.toLowerCase().contains(word)) {
                return word;
            }
        }
        return "";
    }
    //身份证信息泄露
    private Matcher searchID(String content) {
        String pattern = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        Pattern r = Pattern.compile(pattern);
        return r.matcher(content);
    }

    private Matcher searchMobile(String content) {
        String pattern = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";
        Pattern r = Pattern.compile(pattern);
        return r.matcher(content);
    }

    private String matchToString(Matcher m){
        String rst = "";
        for (int i=0; i< m.groupCount(); i++){
            rst += m.group(i) + "<br>";
        }
        return rst;
    }
}
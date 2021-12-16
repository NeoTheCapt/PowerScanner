package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.Global;
import BrianW.AKA.BigChan.Tools.DomainTool;
import BrianW.AKA.BigChan.Tools.Utils;
import burp.*;
import com.r4v3zn.fofa.core.DO.FofaData;
import com.r4v3zn.fofa.core.client.FofaClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetFofaInfo extends Reporter {
    protected IBurpExtenderCallbacks callbacks;
    protected IExtensionHelpers helpers;
    private IBurpCollaboratorClientContext collaboratorContext;

    public GetFofaInfo(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
        super(callbacks, helpers);
        this.callbacks = callbacks;
        this.helpers = helpers;
        collaboratorContext = callbacks.createBurpCollaboratorClientContext();
    }

    public List<IScanIssue> doGetFofaInfo_Icon(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
        List<IScanIssue> issues = new ArrayList<>();
        IHttpRequestResponse pairNegative = fetchURL(baseRequestResponse, "/" + Utils.getRandomString(10));
        String email = Global.config.getConfigFofa_Email_value();
        String key = Global.config.getConfigFofa_ApiKey_value();
        FofaClient client = new FofaClient(email, key);
        //callbacks.printOutput("if enable fofa info based on icon: " + Global.config.getConfigFofa_Ico_value());
        if (!Global.config.getConfigFofa_Ico_value()) {
            return issues;
        }
        URL baseUrl = callbacks.getHelpers().analyzeRequest(baseRequestResponse).getUrl();
        callbacks.printOutput("fofa info based on icon url: " + baseUrl.toString());
        try {
            String StrBaseUrl = Utils.getBaseUrl(baseUrl);
            callbacks.printOutput("fofa info based on icon StrBaseUrl: " + StrBaseUrl);
            String iconPath = Utils.getIconUrlString(new URL(StrBaseUrl));
            String iconUrl = StrBaseUrl + iconPath;
            callbacks.printOutput("Get iconUrl: " + iconUrl);
            assert iconPath != null;
            if (iconPath.replaceAll("/", "").equals("")) {
                callbacks.printOutput("fofa info based on icon found no icon file: " + StrBaseUrl);
                return issues;
            }
            byte[] iconResp = Utils.httpGet(iconUrl);
//            callbacks.printOutput("fofa info based on icon iconResp: " + callbacks.getHelpers().bytesToString(iconResp));
            String iconB64 = callbacks.getHelpers().base64Encode(iconResp);
//            callbacks.printOutput("fofa info based on icon base64: " + iconB64);
            String q = String.format("icon_hash=\"%s\"", Utils.iconb64Hash(iconB64));
            FofaData fofaData = client.getData(q);
            callbacks.printOutput("Get fofa info based on icon: " + fofaData);
            List<List<String>> fofaResults = fofaData.getResults();
            if (!fofaResults.isEmpty()){
                issues.add(
                        reporter(
                                "Fofa Information found (ICON)",
                                String.format("Info: <br>%s <br>"+
                                        "Link: <br>%s <br>",
                                        fofaData,
                                        parseResults(fofaResults)
                                ),
                                "Low",
                                "Certain",
                                baseRequestResponse
                        )
                );
            }
        } catch (Exception e) {
            callbacks.printError(String.format("Error in doGetFofaInfo: %s", Utils.getStackMsg(e)));
            return issues;
        }
        //}
        return issues;
    }

    public List<IScanIssue> doGetFofaInfo_Title(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
        List<IScanIssue> issues = new ArrayList<>();
        IHttpRequestResponse pairNegative = fetchURL(baseRequestResponse, "/" + Utils.getRandomString(10));
        String email = Global.config.getConfigFofa_Email_value();
        String key = Global.config.getConfigFofa_ApiKey_value();
        FofaClient client = new FofaClient(email, key);
        //callbacks.printOutput("if enable fofa info based on title: " + Global.config.getConfigFofa_Title_value());
        if (!Global.config.getConfigFofa_Title_value()) {
            return issues;
        }
        byte[] responseBody = Arrays.copyOfRange(
                baseRequestResponse.getResponse(),
                callbacks.getHelpers().analyzeResponse(baseRequestResponse.getResponse()).getBodyOffset(),
                baseRequestResponse.getResponse().length
        );
        URL baseUrl = callbacks.getHelpers().analyzeRequest(baseRequestResponse).getUrl();
        String title = Utils.getWebsiteTitle(baseUrl);
        callbacks.printOutput("fofa info based on Title: title = " + title);
        if (title.equals("0")|| title.equals("")){
            return issues;
        }
        try {
            String q = String.format("title=\"%s\"", title);
            FofaData fofaData = client.getData(q);
            callbacks.printOutput("Get fofa info based on title: " + fofaData);
            List<List<String>> fofaResults = fofaData.getResults();
            if (!fofaResults.isEmpty()){
                issues.add(
                        reporter(
                                "Fofa Information found (Title)",
                                String.format("Info: <br>%s <br>"+
                                                "Link: <br>%s <br>",
                                        fofaData,
                                        parseResults(fofaResults)
                                ),
                                "Low",
                                "Certain",
                                baseRequestResponse
                        )
                );
            }
        } catch (Exception e) {
            callbacks.printError(String.format("Error in doGetFofaInfo: %s", Utils.getStackMsg(e)));
            return issues;
        }
        //}
        return issues;
    }

    public List<IScanIssue> doGetFofaInfo_Domain(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
        List<IScanIssue> issues = new ArrayList<>();
        IHttpRequestResponse pairNegative = fetchURL(baseRequestResponse, "/" + Utils.getRandomString(10));
        String email = Global.config.getConfigFofa_Email_value();
        String key = Global.config.getConfigFofa_ApiKey_value();
        FofaClient client = new FofaClient(email, key);
        //callbacks.printOutput("if enable fofa info based on Domain: " + Global.config.getConfigFofa_Domain_value());
        if (!Global.config.getConfigFofa_Domain_value()) {
            return issues;
        }
        URL baseUrl = callbacks.getHelpers().analyzeRequest(baseRequestResponse).getUrl();
        String topDomain = DomainTool.getDomainName(baseUrl);
        if (topDomain.equals("")){
            return issues;
        }
        try {
            String q = String.format("domain=\"%s\"", topDomain);
            FofaData fofaData = client.getData(q);
            callbacks.printOutput("Get fofa info based on domain: " + fofaData);
            List<List<String>> fofaResults = fofaData.getResults();
            if (!fofaResults.isEmpty()){
                issues.add(
                        reporter(
                                "Fofa Information found (Domain)",
                                String.format("Info: <br>%s <br>"+
                                                "Link: <br>%s <br>",
                                        fofaData,
                                        parseResults(fofaResults)
                                ),
                                "Low",
                                "Certain",
                                baseRequestResponse
                        )
                );
            }
        } catch (Exception e) {
            callbacks.printError(String.format("Error in doGetFofaInfo: %s", Utils.getStackMsg(e)));
            return issues;
        }
        //}
        return issues;
    }
    private IHttpRequestResponse fetchURL(IHttpRequestResponse basePair, String newPath) {
        String path = this.helpers.analyzeRequest(basePair).getUrl().getPath();
        String newReq = new String(basePair.getRequest()).replace(path, newPath);
        return callbacks.makeHttpRequest(basePair.getHttpService(), newReq.getBytes());
    }

    private IHttpRequestResponse fetchURLWithNewReq(IHttpRequestResponse basePair, String path) throws MalformedURLException {
        URL oldURL = this.helpers.analyzeRequest(basePair).getUrl();
        String baseURL = Utils.getBaseUrl(oldURL);
        byte[] newReq = this.helpers.buildHttpRequest(new URL(baseURL + path));
        return callbacks.makeHttpRequest(basePair.getHttpService(), newReq);
    }
    private String parseResults(List<List<String>> fofaResults){
        StringBuilder rst = new StringBuilder();
//        System.out.println(fofaResults.get(0).getClass());
        for (Object fofaResult:  fofaResults){
            if (fofaResult.toString().contains("://")){
                rst.append(String.format("<a href=\"%s\">%s</a><br>",
                        fofaResult,
                        fofaResult
                        ));
            }else{
                rst.append(fofaResult).append("<br>");
            }
        }
        return rst.toString();
    }
}
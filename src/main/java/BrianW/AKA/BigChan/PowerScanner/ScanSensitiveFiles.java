package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.Global;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScanSensitiveFiles extends Reporter {
    protected IBurpExtenderCallbacks callbacks;
    protected IExtensionHelpers helpers;
    private IBurpCollaboratorClientContext collaboratorContext;

    public ScanSensitiveFiles(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
        super(callbacks, helpers);
        this.callbacks = callbacks;
        this.helpers = helpers;
        collaboratorContext = callbacks.createBurpCollaboratorClientContext();
    }

    public List<IScanIssue> doScanSensiveFiles(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
        String[] fileList = Global.config.getConfigSensitiveFiles_value().split("\n");
        List<IScanIssue> issues = new ArrayList<>();
        IHttpRequestResponse pairNegative = fetchURL(baseRequestResponse, "/" + utils.getRandomString(10));
        for (String file : fileList) {
            file = file.replace("\r", "");
            if ("".equals(file.replace(" ", ""))) {
                continue;
            }
            file = "/" + file;
            IHttpRequestResponse pairSensitiveFile = null;
            try {
                pairSensitiveFile = fetchURLWithNewReq(baseRequestResponse, file);
            } catch (Exception e) {
                callbacks.printOutput("error in doScanSensiveFiles: " + utils.getStackMsg(e));
                return null;
            }

            assert pairSensitiveFile != null;
            short code = helpers.analyzeResponse(pairSensitiveFile.getResponse()).getStatusCode();
            callbacks.printOutput(String.format("Scanning sensitive file: %s, code: %d", file, code));
            IResponseVariations responseAnalyze = callbacks.getHelpers().analyzeResponseVariations(
                    pairSensitiveFile.getResponse(),
                    pairNegative.getResponse()
            );
            if (
                    (utils.rangeInDefined(code, 200, 399) || code == 403)
                    && responseAnalyze.getAttributeValue("visible_text", 0) != responseAnalyze.getAttributeValue("visible_text", 1)
                    )
            {

                issues.add(
                        reporter(
                                "Sensitive File found",
                                String.format("Filename: %s <br>" +
                                                "Response Status Code: %d <br>",
//                                                "Response SensitiveFile: %s <br>" +
//                                                "Response Negative: %s <br>",
                                        file,
                                        code
//                                        callbacks.getHelpers().base64Encode(pairSensitiveFile.getResponse()),
//                                        callbacks.getHelpers().base64Encode(pairNegative.getResponse())
                                ),
                                "Low",
                                "Firm",
                                pairSensitiveFile,
                                pairNegative
                        )
                );
            }
        }
        return issues;
    }

    private IHttpRequestResponse fetchURL(IHttpRequestResponse basePair, String newPath) {
        String path = this.helpers.analyzeRequest(basePair).getUrl().getPath();
        String newReq = new String(basePair.getRequest()).replace(path, newPath);
        return callbacks.makeHttpRequest(basePair.getHttpService(), newReq.getBytes());
    }

    private IHttpRequestResponse fetchURLWithNewReq(IHttpRequestResponse basePair, String path) throws MalformedURLException {
        URL oldURL = this.helpers.analyzeRequest(basePair).getUrl();
        String baseURL = utils.getBaseUrl(oldURL);
        byte[] newReq = this.helpers.buildHttpRequest(new URL(baseURL + path));
        return callbacks.makeHttpRequest(basePair.getHttpService(), newReq);
    }

}

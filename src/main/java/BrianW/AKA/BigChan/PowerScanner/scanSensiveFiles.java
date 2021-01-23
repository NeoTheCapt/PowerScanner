package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.InteractionServer;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

public class scanSensiveFiles extends scanHandler {
	protected IBurpExtenderCallbacks callbacks;
	protected IExtensionHelpers helpers;
	private IBurpCollaboratorClientContext collaboratorContext;
	
	public scanSensiveFiles(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
		super(callbacks, helpers);
		this.callbacks = callbacks;
		this.helpers = helpers;
		collaboratorContext = callbacks.createBurpCollaboratorClientContext();
	}
	IScanIssue doScanSensiveFiles(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
		byte[] resp = baseRequestResponse.getResponse();
		String baseName = insertionPoint.getInsertionPointName();
		String insertionPointType = utils.bytesToHexString(new byte[]{insertionPoint.getInsertionPointType()}, 1);
		String baseValue = insertionPoint.getBaseValue();
		InteractionServer interactionServer = new InteractionServer();
		
		return null;
	}
}

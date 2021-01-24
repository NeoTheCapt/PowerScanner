package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.hitRst;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;


public class ScanSensitiveParam extends Reporter {
	protected IBurpExtenderCallbacks callbacks;
	protected IExtensionHelpers helpers;
	protected String[] sensitiveWords_file = new String[]{"file", "down", "path", "template"};
	protected String[] sensitiveWords_sql = new String[]{"order", "desc", "limit", "table", "db"};
	
	public ScanSensitiveParam(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
		super(callbacks, helpers);
		this.callbacks = callbacks;
		this.helpers = helpers;
	}
	
	public IScanIssue doScanSensitiveParam(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
		byte[] resp = baseRequestResponse.getResponse();
		byte[] req = baseRequestResponse.getRequest();
		String baseName = insertionPoint.getInsertionPointName();
		String insertionPointType = utils.bytesToHexString(new byte[]{insertionPoint.getInsertionPointType()}, 1);
		String baseValue = insertionPoint.getBaseValue();
		String word = searchSensitiveWords(baseName);
		if (!word.equals("")) {
			return reporter(
					"Sensitive word found in param.",
					String.format("param: %s <br>" +
									"Contains sensitive word: %s<br>" +
									"This sensitive param maybe vulnerable"
							,
							baseName,
							word
					),
					"Info",
					baseRequestResponse
			);
		}
		
		return null;
	}
	
	private String searchSensitiveWords(String param) {
		for (String word : sensitiveWords_file) {
			if (param.contains(word)) {
				return word;
			}
		}
		for (String word : sensitiveWords_sql) {
			if (param.contains(word)) {
				return word;
			}
		}
		return "";
	}
}

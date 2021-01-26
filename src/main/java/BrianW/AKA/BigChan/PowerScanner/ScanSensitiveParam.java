package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.hitRst;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

import java.util.ArrayList;
import java.util.List;

public class ScanSensitiveParam extends Reporter {
	protected IBurpExtenderCallbacks callbacks;
	protected IExtensionHelpers helpers;
	protected String[] sensitiveWords = new String[]{
			"file", "down", "path", "template",
			"order", "desc", "limit", "table",
			"@type"
	};

	public ScanSensitiveParam(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
		super(callbacks, helpers);
		this.callbacks = callbacks;
		this.helpers = helpers;
	}
	
	public List<IScanIssue> doScanSensitiveParam(IHttpRequestResponse baseRequestResponse) {
		List<IScanIssue> issues = new ArrayList<>();
		List<IParameter> params  = helpers.analyzeRequest(baseRequestResponse).getParameters();
		for (IParameter param : params){
			String word = searchSensitiveWords(param.getName());
			if (!word.equals("")) {
				issues.add(reporter(
						"Sensitive word found in param.",
						String.format("param: %s <br>" +
										"Contains sensitive word: %s<br>" +
										"This sensitive param maybe vulnerable"
								,
								param.getName(),
								word
						),
						"Information",
						"Firm",
						baseRequestResponse
				));
			}
		}
		return issues;
	}
	
	private String searchSensitiveWords(String param) {
		for (String word : sensitiveWords) {
			if (param.toLowerCase().contains(word)) {
				return word;
			}
		}
		return "";
	}
}

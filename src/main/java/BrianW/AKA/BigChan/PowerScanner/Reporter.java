package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.CustomScanIssue;
import BrianW.AKA.BigChan.Tools.hitRst;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

import java.net.URL;
import java.util.Arrays;

public class Reporter {
	private final IBurpExtenderCallbacks callbacks;
	private final IExtensionHelpers helpers;
	public Reporter(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
		this.callbacks = callbacks;
		this.helpers = helpers;
	}
	CustomScanIssue reporter(String title, String desc, String sev, IHttpRequestResponse... Pairs) {
		IHttpService service = Pairs[0].getHttpService();
		URL url = helpers.analyzeRequest(Pairs[0]).getUrl();
		title = "[PowerScanner]: " + title;
//		IHttpRequestResponse[] finalPair = new IHttpRequestResponse[]{
//				basePair,
//				Pairs
//		};
		return new CustomScanIssue(
				service,
				url,
				Pairs,
				title,
				desc,
				sev);
	}
	
	
}

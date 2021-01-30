package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.hitRst;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

public class ScanSqli extends Reporter {
	protected IBurpExtenderCallbacks callbacks;
	protected IExtensionHelpers helpers;
	public ScanSqli(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
		super(callbacks, helpers);
		this.callbacks = callbacks;
		this.helpers = helpers;
	}
//@todo where sort order limit table from start end
public IScanIssue doScanSqli(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
		byte[] resp = baseRequestResponse.getResponse();
		String baseName = insertionPoint.getInsertionPointName();
		String InsertionPointType = utils.bytesToHexString(new byte[]{insertionPoint.getInsertionPointType()}, 1);
		String baseValue = insertionPoint.getBaseValue();
		String testStr = "";
		String evilStr = "";
//		callbacks.printOutput("baseName=" + baseName + ", baseValue=" + baseValue);
		String baseType = baseValue.matches("[0-9]+") ? "int" : "string";
//		if (baseValue.matches("[0-9]+") && baseName.length()<5 && baseName.contains("id")){
		if ("int".equals(baseType)) {
			testStr = "/1";
			evilStr = "/0";
		} else {
			testStr = "''";
			evilStr = "'";
		}
		
		byte[] reqTest = insertionPoint.buildRequest((baseValue + testStr).getBytes());
		IHttpRequestResponse pairTrue = callbacks.makeHttpRequest(
				baseRequestResponse.getHttpService(),
				reqTest
		);
		byte[] respTrue = pairTrue.getResponse();
		
		byte[] reqEvil = insertionPoint.buildRequest((baseValue + evilStr).getBytes());
		IHttpRequestResponse pairEvil = callbacks.makeHttpRequest(
				baseRequestResponse.getHttpService(),
				reqEvil
		);
		byte[] respEvil = pairEvil.getResponse();
		hitRst hit = hit(resp, respTrue, respEvil, testStr, evilStr);
		if (hit.getCdoe() > 0) {
//			callbacks.printOutput("injection found in param: " + baseName + " with type " + baseType + " ;InsertionPointType:" + InsertionPointType);
			return reporter(
					"injection(might be SQLi) found",
					String.format("param: %s <br>" +
									"type: %s <br>" +
									"InsertionPointType: %s <br>" +
									"HitCode: %s <br><br>" +
									"The same between base response and negative response: <br>%s<br>" +
									"The difference between base response and negative response: <br>%s<br>" +
									"The same between base response and positive response: <br>%s<br>" +
									"The difference between base response and positive response: <br>%s<br>"
							,
							baseName,
							baseType,
							InsertionPointType,
							hit.getCdoe(),
							hit.getCompareWithNegative_Same(),
							hit.getCompareWithNegative_Diff(),
							hit.getCompareWithPositive_Same(),
							hit.getCompareWithPositive_Diff()
					),
					"High",
					"Firm",
					baseRequestResponse,
					pairTrue,
					pairEvil
			);
		}
		return null;
	}
	
	
}

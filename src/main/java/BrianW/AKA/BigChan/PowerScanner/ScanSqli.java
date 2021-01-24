package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.hitRst;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

public class ScanSqli extends scanHandler{
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
		callbacks.printOutput("baseName=" + baseName + ", baseValue=" + baseValue);
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
			callbacks.printOutput("injection found in param: " + baseName + " with type " + baseType + " ;InsertionPointType:" + InsertionPointType);
			// get the offsets of the payload within the request, for in-UI highlighting
//			List<int[]> requestHighlights_origin = new ArrayList<>(1);
//			requestHighlights_origin.add(insertionPoint.getPayloadOffsets(baseValue.getBytes()));
			return reporter(
					"injection(might be SQLi) found",
					String.format("param: %s <br>" +
									"type: %s <br>" +
									"InsertionPointType: %s <br>" +
									"HitCode: %s <br><br>" +
									"compareRestEvil SAME: <br>%s<br>" +
									"compareRestEvil Different: <br>%s<br>"
							,
							baseName,
							baseType,
							InsertionPointType,
							hit.getCdoe(),
							hit.getCompareWithRespEvil_Same(),
							hit.getCompareWithRespEvil_Diff()
					),
					"High",
					baseRequestResponse,
					pairTrue,
					pairEvil
			);
		}
		return null;
	}
}

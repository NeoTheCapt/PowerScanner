package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.hitRst;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

public class ScanPathTraversal extends Reporter {
	protected IBurpExtenderCallbacks callbacks;
	protected IExtensionHelpers helpers;
	
	public ScanPathTraversal(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
		super(callbacks, helpers);
		this.callbacks = callbacks;
		this.helpers = helpers;
	}
	
	public IScanIssue doScanPathTraversal(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
		byte[] resp = baseRequestResponse.getResponse();
		byte[] req = baseRequestResponse.getRequest();
		String baseName = insertionPoint.getInsertionPointName();
		String insertionPointType = utils.bytesToHexString(new byte[]{insertionPoint.getInsertionPointType()}, 1);
		String baseValue = insertionPoint.getBaseValue();
		
		if (baseValue.contains("/")) {
			String strPositive = "/./";
			String strNegative = "/z/";
			byte[] reqPositive = insertionPoint.buildRequest((baseValue.replace("/", strPositive)).getBytes());
			IHttpRequestResponse pairPositive = callbacks.makeHttpRequest(
					baseRequestResponse.getHttpService(),
					reqPositive
			);
			byte[] respPositive = pairPositive.getResponse();
			byte[] reqNegative = insertionPoint.buildRequest((baseValue.replace("/", strNegative)).getBytes());
			IHttpRequestResponse pairNegative = callbacks.makeHttpRequest(
					baseRequestResponse.getHttpService(),
					reqNegative
			);
			byte[] respNegative = pairNegative.getResponse();
			hitRst hit = hit(resp, respPositive, respNegative, strPositive, strNegative);
			if (hit.getCdoe() > 0) {
				return reporter(
						"PathTraversal found",
						String.format("param: %s <br>" +
										"HitCode: %s <br><br>" +
										"compareRestEvil SAME: <br>%s<br>" +
										"compareRestEvil Different: <br>%s<br>"
								,
								baseName,
								hit.getCdoe(),
								hit.getCompareWithRespEvil_Same(),
								hit.getCompareWithRespEvil_Diff()
						),
						"High",
						baseRequestResponse,
						pairPositive,
						pairNegative
				);
			}
			return null;
		}
		String strPositive = "/";
		String strNegative = "z";
		byte[] reqPositive = insertionPoint.buildRequest((strPositive + baseValue).getBytes());
		IHttpRequestResponse pairPositive = callbacks.makeHttpRequest(
				baseRequestResponse.getHttpService(),
				reqPositive
		);
		byte[] respPositive = pairPositive.getResponse();
		byte[] reqNegative = insertionPoint.buildRequest((strNegative + baseValue).getBytes());
		IHttpRequestResponse pairNegative = callbacks.makeHttpRequest(
				baseRequestResponse.getHttpService(),
				reqNegative
		);
		byte[] respNegative = pairNegative.getResponse();
		hitRst hit = hit(resp, respPositive, respNegative, strPositive, strNegative);
		if (hit.getCdoe() > 0) {
			return reporter(
					"PathTraversal found",
					String.format("param: %s <br>" +
									"HitCode: %s <br><br>" +
									"compareRestEvil SAME: <br>%s<br>" +
									"compareRestEvil Different: <br>%s<br>"
							,
							baseName,
							hit.getCdoe(),
							hit.getCompareWithRespEvil_Same(),
							hit.getCompareWithRespEvil_Diff()
					),
					"High",
					baseRequestResponse,
					pairPositive,
					pairNegative
			);
		}
		return null;
	}
}

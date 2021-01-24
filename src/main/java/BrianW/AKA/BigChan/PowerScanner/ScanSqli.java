package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.hitRst;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

import java.util.Arrays;

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
	
	private hitRst hit(byte[] resp, byte[] respTrue, byte[] respEvil, String testStr, String evilStr) {
		int resp_statusCode = helpers.analyzeResponse(resp).getStatusCode();
		int respTrue_statusCode = helpers.analyzeResponse(respTrue).getStatusCode();
		int respEvil_statusCode = helpers.analyzeResponse(respEvil).getStatusCode();
		IResponseVariations ResponseVariations1 = helpers.analyzeResponseVariations(resp, respTrue);
		IResponseVariations ResponseVariations2 = helpers.analyzeResponseVariations(resp, respEvil);
		StringBuilder compareWithRespEvil_Diff = new StringBuilder();
		StringBuilder compareWithRespEvil_Same = new StringBuilder();
		for (String VariantAttribute : ResponseVariations2.getVariantAttributes()) {
			compareWithRespEvil_Diff.append(String.format("%s : %s vs %s<br>",
					VariantAttribute,
					ResponseVariations2.getAttributeValue(VariantAttribute, 0),
					ResponseVariations2.getAttributeValue(VariantAttribute, 1)
			));
//			callbacks.printOutput("resp and respEvil has VariantAttribute = " + VariantAttribute + "; "
//					+ ResponseVariations2.getAttributeValue(VariantAttribute, 0) + " vs "
//					+ ResponseVariations2.getAttributeValue(VariantAttribute, 1)
//			);
		}
		for (String InvariantAttribute : ResponseVariations2.getInvariantAttributes()) {
			compareWithRespEvil_Same.append(String.format("%s : %s vs %s<br>",
					InvariantAttribute,
					ResponseVariations2.getAttributeValue(InvariantAttribute, 0),
					ResponseVariations2.getAttributeValue(InvariantAttribute, 1)
			));
//			callbacks.printOutput("resp and respEvil has InvariantAttribute = " + InvariantAttribute + "; "
//					+ ResponseVariations2.getAttributeValue(InvariantAttribute, 0) + " vs "
//					+ ResponseVariations2.getAttributeValue(InvariantAttribute, 1)
//			);
		}
		if (resp_statusCode == respTrue_statusCode && resp_statusCode != respEvil_statusCode) {
			return new hitRst(1, compareWithRespEvil_Diff.toString(), compareWithRespEvil_Same.toString());
		}
		int resp_ErrorCount = utils.countStr(Arrays.toString(resp), "error");
		int respTrue_ErrorCount = utils.countStr(Arrays.toString(respTrue), "error");
		int respEvil_ErrorCount = utils.countStr(Arrays.toString(respEvil), "error");
		if (resp_ErrorCount == respTrue_ErrorCount && respTrue_ErrorCount != respEvil_ErrorCount) {
			return new hitRst(2, compareWithRespEvil_Diff.toString(), compareWithRespEvil_Same.toString());
		}
		String respTrue_pure = Arrays.toString(respTrue).replace(testStr, "").replace(evilStr, "");
		//callbacks.printOutput("respTrue_pure=" + respTrue_pure);
		String respEvil_pure = Arrays.toString(respEvil).replace(evilStr, "").replace(testStr, "");
		//callbacks.printOutput("respEvil_pure=" + respEvil_pure);
		
		//去除payload后，如果test返回和Evil返回一样，无漏洞
		if (respTrue_pure.equals(respEvil_pure)) {
			return new hitRst(0, compareWithRespEvil_Diff.toString(), compareWithRespEvil_Same.toString());
		}

//		callbacks.printOutput(Integer.toString(ResponseVariations2.getAttributeValue("whole_body_content", 0)));
		
		//如果test包和原始包不一样，无漏洞
		if (ResponseVariations1.getVariantAttributes().contains("initial_body_content") ||
				ResponseVariations1.getVariantAttributes().contains("content_type")
		) {
			return new hitRst(0, compareWithRespEvil_Diff.toString(), compareWithRespEvil_Same.toString());
		}
		//如果evil包和原始包一样，无漏洞
		if (ResponseVariations2.getInvariantAttributes().contains("whole_body_content")
				|| ResponseVariations2.getInvariantAttributes().contains("content_length")
//				|| ResponseVariations2.getInvariantAttributes().contains("visible_text")
		) {
			return new hitRst(0, compareWithRespEvil_Diff.toString(), compareWithRespEvil_Same.toString());
		}
		int length1 = ResponseVariations1.getAttributeValue("content_length", 1) - ResponseVariations2.getAttributeValue("content_length", 0);
		int length2 = ResponseVariations2.getAttributeValue("content_length", 1) - ResponseVariations2.getAttributeValue("content_length", 0);
//		callbacks.printOutput("respTrueLength - respLength=" + Integer.toString(length1));
//		callbacks.printOutput(Integer.toString(testStr.length()));
//		callbacks.printOutput("respEvilLength - respLength=" + Integer.toString(length2));
//		callbacks.printOutput(Integer.toString(evilStr.length()));
		if (length1 / testStr.length() == length2 / evilStr.length()) {
			return new hitRst(0, compareWithRespEvil_Diff.toString(), compareWithRespEvil_Same.toString());
		}
		
		if (ResponseVariations1.getInvariantAttributes() != ResponseVariations2.getInvariantAttributes()
				&&
				ResponseVariations1.getAttributeValue("content_length", 1) != ResponseVariations2.getAttributeValue("content_length", 1)
		) {
			return new hitRst(3, compareWithRespEvil_Diff.toString(), compareWithRespEvil_Same.toString());
		}
		return new hitRst(0, compareWithRespEvil_Diff.toString(), compareWithRespEvil_Same.toString());
	}
}

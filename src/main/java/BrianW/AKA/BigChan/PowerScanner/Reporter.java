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
	hitRst hit(byte[] resp, byte[] respTrue, byte[] respEvil, String testStr, String evilStr) {
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

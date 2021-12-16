package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.CustomScanIssue;
import BrianW.AKA.BigChan.Tools.HitRst;
import BrianW.AKA.BigChan.Tools.Utils;
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
	CustomScanIssue reporter(String title, String desc, String sev, String confidence, IHttpRequestResponse... Pairs) {
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
				sev,
				confidence);
	}
	HitRst hit(byte[] respBase, byte[] respPositive, byte[] respNegative, String positiveStr, String negativeStr) {
		int resp_statusCode = helpers.analyzeResponse(respBase).getStatusCode();
		int respTrue_statusCode = helpers.analyzeResponse(respPositive).getStatusCode();
		int respEvil_statusCode = helpers.analyzeResponse(respNegative).getStatusCode();
		IResponseVariations ResponseVariationsPositive = helpers.analyzeResponseVariations(respBase, respPositive);
		IResponseVariations ResponseVariationsNegative = helpers.analyzeResponseVariations(respBase, respNegative);
		StringBuilder compareWithNegative_Diff = new StringBuilder();
		StringBuilder compareWithNegative_Same = new StringBuilder();
		StringBuilder compareWithPositive_Diff = new StringBuilder();
		StringBuilder compareWithPositive_Same = new StringBuilder();
		for (String VariantAttribute : ResponseVariationsNegative.getVariantAttributes()) {
			compareWithNegative_Diff.append(String.format("%s : %s vs %s<br>",
					VariantAttribute,
					ResponseVariationsNegative.getAttributeValue(VariantAttribute, 0),
					ResponseVariationsNegative.getAttributeValue(VariantAttribute, 1)
			));
		}
		for (String InvariantAttribute : ResponseVariationsNegative.getInvariantAttributes()) {
			compareWithNegative_Same.append(String.format("%s : %s vs %s<br>",
					InvariantAttribute,
					ResponseVariationsNegative.getAttributeValue(InvariantAttribute, 0),
					ResponseVariationsNegative.getAttributeValue(InvariantAttribute, 1)
			));
		}
		for (String VariantAttribute : ResponseVariationsPositive.getVariantAttributes()) {
			compareWithPositive_Diff.append(String.format("%s : %s vs %s<br>",
					VariantAttribute,
					ResponseVariationsPositive.getAttributeValue(VariantAttribute, 0),
					ResponseVariationsPositive.getAttributeValue(VariantAttribute, 1)
			));
		}
		for (String InvariantAttribute : ResponseVariationsPositive.getInvariantAttributes()) {
			compareWithPositive_Same.append(String.format("%s : %s vs %s<br>",
					InvariantAttribute,
					ResponseVariationsPositive.getAttributeValue(InvariantAttribute, 0),
					ResponseVariationsPositive.getAttributeValue(InvariantAttribute, 1)
			));
		}
		if (resp_statusCode == respTrue_statusCode && resp_statusCode != respEvil_statusCode) {
			return new HitRst(1, compareWithNegative_Diff.toString(), compareWithNegative_Same.toString(), compareWithPositive_Diff.toString(), compareWithPositive_Same.toString());
		}
		int resp_ErrorCount = Utils.countStr(Arrays.toString(respBase), "error");
		int respTrue_ErrorCount = Utils.countStr(Arrays.toString(respPositive), "error");
		int respEvil_ErrorCount = Utils.countStr(Arrays.toString(respNegative), "error");
		if (resp_ErrorCount == respTrue_ErrorCount && respTrue_ErrorCount != respEvil_ErrorCount) {
			return new HitRst(2, compareWithNegative_Diff.toString(), compareWithNegative_Same.toString(), compareWithPositive_Diff.toString(), compareWithPositive_Same.toString());
		}
		//如果原始包visible_text和visible_word_count都是0，基本可以断定是验证码类型
		if (ResponseVariationsPositive.getAttributeValue("visible_text", 0) == 0 && ResponseVariationsPositive.getAttributeValue("visible_word_count", 0) == 0) {
			return new HitRst(0, compareWithNegative_Diff.toString(), compareWithNegative_Same.toString(), compareWithPositive_Diff.toString(), compareWithPositive_Same.toString());
		}
		//去除payload后，如果positive返回和negative返回一样，无漏洞
		String respTrue_pure = Arrays.toString(respPositive).replace(positiveStr, "").replace(negativeStr, "");
		String respEvil_pure = Arrays.toString(respNegative).replace(negativeStr, "").replace(positiveStr, "");
		if (respTrue_pure.equals(respEvil_pure)) {
			return new HitRst(0, compareWithNegative_Diff.toString(), compareWithNegative_Same.toString(), compareWithPositive_Diff.toString(), compareWithPositive_Same.toString());
		}
		
		//如果positive包和原始包不一样，无漏洞
		if (ResponseVariationsPositive.getVariantAttributes().contains("initial_body_content") ||
				ResponseVariationsPositive.getVariantAttributes().contains("content_type")
		) {
			return new HitRst(0, compareWithNegative_Diff.toString(), compareWithNegative_Same.toString(), compareWithPositive_Diff.toString(), compareWithPositive_Same.toString());
		}
		//如果Negative包和原始包一样，无漏洞
		if (ResponseVariationsNegative.getInvariantAttributes().contains("whole_body_content")
				|| ResponseVariationsNegative.getInvariantAttributes().contains("content_length")
		) {
			return new HitRst(0, compareWithNegative_Diff.toString(), compareWithNegative_Same.toString(), compareWithPositive_Diff.toString(), compareWithPositive_Same.toString());
		}
		//如果Negative包和原始包字符数差==positive包和原始包字符数差，无漏洞
		int positiveLength = ResponseVariationsPositive.getAttributeValue("content_length", 1);
		int negativeLength = ResponseVariationsNegative.getAttributeValue("content_length", 1);
		int baseLength = ResponseVariationsNegative.getAttributeValue("content_length", 0);
		
		int length1 = positiveLength - baseLength;
		int length2 = negativeLength - baseLength;
		if (length1 / positiveStr.length() == length2 / negativeStr.length()) {
			return new HitRst(0, compareWithNegative_Diff.toString(), compareWithNegative_Same.toString(), compareWithPositive_Diff.toString(), compareWithPositive_Same.toString());
		}
		//如果Negative包比positive包多一个字符，无漏洞
		if (positiveLength - negativeLength == positiveStr.length() - negativeStr.length()) {
			return new HitRst(0, compareWithNegative_Diff.toString(), compareWithNegative_Same.toString(), compareWithPositive_Diff.toString(), compareWithPositive_Same.toString());
		}
		
		if (ResponseVariationsPositive.getInvariantAttributes() != ResponseVariationsNegative.getInvariantAttributes()
				&&
				ResponseVariationsPositive.getAttributeValue("content_length", 1) != ResponseVariationsNegative.getAttributeValue("content_length", 1)
		) {
			return new HitRst(3, compareWithNegative_Diff.toString(), compareWithNegative_Same.toString(), compareWithPositive_Diff.toString(), compareWithPositive_Same.toString());
		}
		return new HitRst(0, compareWithNegative_Diff.toString(), compareWithNegative_Same.toString(), compareWithPositive_Diff.toString(), compareWithPositive_Same.toString());
	}
	
}

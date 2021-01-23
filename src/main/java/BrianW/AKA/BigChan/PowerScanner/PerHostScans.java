package BrianW.AKA.BigChan.PowerScanner;
import burp.*;

import java.util.ArrayList;
import java.util.List;

public class PerHostScans implements IScannerCheck {
	private final IBurpExtenderCallbacks callbacks;
	private final IExtensionHelpers helpers;
	private final List<String> scanedHosts;
	
	public PerHostScans(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
		this.callbacks = callbacks;
		this.helpers = helpers;
		this.scanedHosts = new ArrayList<String>();;
	}
	
	@Override
	public List<IScanIssue> doPassiveScan(IHttpRequestResponse baseRequestResponse) {
		return null;
	}
	
	@Override
	public List<IScanIssue> doActiveScan(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
		// report the issue
		List<IScanIssue> issues = new ArrayList<>();
		String currentHost = baseRequestResponse.getHttpService().getHost();
		if (this.scanedHosts.contains(currentHost)){
			return issues;
		}
		
		issues.add(
				new scanSensiveFiles(callbacks, helpers).doScanSensiveFiles(baseRequestResponse, insertionPoint)
		);
//		issues.add(
//				new ScanRCE(callbacks, helpers).doScanRCE(baseRequestResponse, insertionPoint)
//		);
		List nullList = new ArrayList();
		nullList.add(null);
		issues.removeAll(nullList);
		return issues;
	}
	
	@Override
	public int consolidateDuplicateIssues(IScanIssue existingIssue, IScanIssue newIssue) {
		return 0;
	}
	
	// helper method to search a response for occurrences of a literal match string
	// and return a list of start/end offsets
	private List<int[]> getMatches(byte[] response, byte[] match) {
		List<int[]> matches = new ArrayList<int[]>();
		
		int start = 0;
		while (start < response.length) {
			start = helpers.indexOf(response, match, true, start, response.length);
			if (start == -1) {
				break;
			}
			matches.add(new int[]{start, start + match.length});
			start += match.length;
		}
		
		return matches;
	}
}
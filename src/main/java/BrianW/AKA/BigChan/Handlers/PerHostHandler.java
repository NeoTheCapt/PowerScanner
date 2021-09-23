package BrianW.AKA.BigChan.Handlers;
import BrianW.AKA.BigChan.PowerScanner.GetFofaInfo;
import BrianW.AKA.BigChan.PowerScanner.ScanSensitiveFiles;
import BrianW.AKA.BigChan.Tools.Global;
import burp.*;

import java.util.ArrayList;
import java.util.List;

public class PerHostHandler implements IScannerCheck {
	private final IBurpExtenderCallbacks callbacks;
	private final IExtensionHelpers helpers;
	private final List<String> scanedHosts;
	
	public PerHostHandler(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
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
		this.callbacks.printOutput("do ActiveScan per host on: " + currentHost);
		scanedHosts.add(currentHost);
		if (Global.config.getConfigSensitiveFilesScanEnable_value()){
			issues.addAll(
					new ScanSensitiveFiles(callbacks, helpers).doScanSensiveFiles(baseRequestResponse, insertionPoint)
			);
		}
		issues.addAll(
				new GetFofaInfo(callbacks, helpers).doGetFofaInfo_Icon(baseRequestResponse, insertionPoint)
		);
		issues.addAll(
				new GetFofaInfo(callbacks, helpers).doGetFofaInfo_Title(baseRequestResponse, insertionPoint)
		);
		issues.addAll(
				new GetFofaInfo(callbacks, helpers).doGetFofaInfo_Domain(baseRequestResponse, insertionPoint)
		);
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
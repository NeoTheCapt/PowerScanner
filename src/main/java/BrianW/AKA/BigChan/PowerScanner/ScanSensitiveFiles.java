package BrianW.AKA.BigChan.PowerScanner;

import BrianW.AKA.BigChan.Tools.Global;
import burp.*;

import java.util.ArrayList;
import java.util.List;

public class ScanSensitiveFiles extends Reporter {
	protected IBurpExtenderCallbacks callbacks;
	protected IExtensionHelpers helpers;
	private IBurpCollaboratorClientContext collaboratorContext;
	
	public ScanSensitiveFiles(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
		super(callbacks, helpers);
		this.callbacks = callbacks;
		this.helpers = helpers;
		collaboratorContext = callbacks.createBurpCollaboratorClientContext();
	}
	
	public List<IScanIssue> doScanSensiveFiles(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
		String[] fileList = Global.config.getConfigSensitiveFiles_value().split("\n");
		List<IScanIssue> issues = new ArrayList<>();
		for (String file : fileList) {
			file = file.replace("\r","");
			if ("".equals(file.replace(" ", ""))){
				continue;
			}
			file = "/" + file;
			IHttpRequestResponse pairSensitiveFile = fetchURL(baseRequestResponse, file);
			short code = helpers.analyzeResponse(pairSensitiveFile.getResponse()).getStatusCode();
			callbacks.printOutput(String.format("Scanning sensitive file: %s, code: %d", file, code));
			if (code == 200 || code == 403 || code == 301 || code == 302) {
				issues.add(
						reporter(
								"Sensitive File found",
								String.format("Filename: %s <br>" +
												"Response Status Code: %d <br>",
										file,
										code
								),
								"Low",
								pairSensitiveFile
						)
				);
			}
		}
		return issues;
	}
	
	private IHttpRequestResponse fetchURL(IHttpRequestResponse basePair, String newPath) {
		String path = this.helpers.analyzeRequest(basePair).getUrl().getPath();
		String newReq = new String(basePair.getRequest()).replace(path, newPath);
		return callbacks.makeHttpRequest(basePair.getHttpService(), newReq.getBytes());
	}
	
}

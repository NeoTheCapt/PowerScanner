package BrianW.AKA.BigChan.Handlers;

import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

import java.util.List;

public class SessionHandler implements ISessionHandlingAction {
	protected IBurpExtenderCallbacks callbacks;
	protected IExtensionHelpers helpers;
	
	public SessionHandler(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
		this.callbacks = callbacks;
		this.helpers = helpers;
	}
	
	@Override
	public String getActionName()
	{
		return "PowerScanner";
	}
	
	@Override
	public void performAction(
			IHttpRequestResponse currentRequest,
			IHttpRequestResponse[] macroItems)
	{
		String randomIP = utils.IpGen();
		byte[] newRequest = new byte[]{};
		String request = helpers.bytesToString(currentRequest.getRequest());
		IRequestInfo requestInfo = helpers.analyzeRequest(currentRequest);
		List<String> headers = requestInfo.getHeaders();
		int bodySize = currentRequest.getRequest().length - requestInfo.getBodyOffset();
		byte[] reqBody = new byte[bodySize];
		callbacks.printOutput("currentRequest.getRequest().length=" + currentRequest.getRequest().length);
		callbacks.printOutput("requestInfo.getBodyOffset()" + requestInfo.getBodyOffset());
		System.arraycopy(currentRequest.getRequest(), requestInfo.getBodyOffset(), reqBody, 0, bodySize);
		headers.removeIf(n -> (n.startsWith("x-originating-IP: ")));
		headers.removeIf(n -> (n.startsWith("x-forwarded-for: ")));
		headers.removeIf(n -> (n.startsWith("x-remote-IP: ")));
		headers.removeIf(n -> (n.startsWith("x-remote-addr: ")));
		headers.removeIf(n -> (n.startsWith("X-Client-IP: ")));
		headers.add("x-originating-IP: "+randomIP);
		headers.add("x-forwarded-for: "+randomIP);
		headers.add("x-remote-IP: "+randomIP);
		headers.add("x-remote-addr: "+randomIP);
		headers.add("X-Client-IP: "+randomIP);
		headers.removeIf(n -> (n.startsWith("Cookie: ")));
		headers.add("Cookie: ");
		newRequest = helpers.buildHttpMessage(headers, reqBody);
		
		currentRequest.setRequest(newRequest);
	}
}

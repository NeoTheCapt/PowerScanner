package BrianW.AKA.BigChan.Handlers;

import BrianW.AKA.BigChan.Tools.Global;
import BrianW.AKA.BigChan.Tools.Utils;
import burp.*;

import java.nio.charset.StandardCharsets;
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
		String randomIP = Utils.IpGen();
		byte[] newRequest = new byte[]{};
		String request = helpers.bytesToString(currentRequest.getRequest());
		IRequestInfo requestInfo = helpers.analyzeRequest(currentRequest);
		List<String> headers = requestInfo.getHeaders();
		int bodySize = currentRequest.getRequest().length - requestInfo.getBodyOffset();
		byte[] reqBody = new byte[bodySize];
//		callbacks.printOutput("currentRequest.getRequest().length=" + currentRequest.getRequest().length);
//		callbacks.printOutput("requestInfo.getBodyOffset()" + requestInfo.getBodyOffset());
		System.arraycopy(currentRequest.getRequest(), requestInfo.getBodyOffset(), reqBody, 0, bodySize);
		if (Global.config.getConfigRandomIPEnable_value()) {
			headers.removeIf(n -> (n.startsWith("x-originating-IP: ")));
			headers.removeIf(n -> (n.startsWith("x-forwarded-for: ")));
			headers.removeIf(n -> (n.startsWith("x-remote-IP: ")));
			headers.removeIf(n -> (n.startsWith("x-remote-addr: ")));
			headers.removeIf(n -> (n.startsWith("X-Client-IP: ")));
			headers.add("x-originating-IP: " + randomIP);
			headers.add("x-forwarded-for: " + randomIP);
			headers.add("x-remote-IP: " + randomIP);
			headers.add("x-remote-addr: " + randomIP);
			headers.add("X-Client-IP: " + randomIP);
		}
		if (Global.config.getConfigClearCookieEnable_value()) {
			headers.removeIf(n -> (n.startsWith("Cookie: ")));
			headers.add("Cookie: ");
		}
		if (Global.config.getConfigRandomUAEnable_value()) {
			headers.removeIf(n -> (n.startsWith("User-Agent: ")));
			headers.add(String.format("User-Agent: %s/%d.0 (Windows NT %d.0; Win%d; x64) AppleWebKit/%d (%s, like %s) %s/%d.0.%d.%d %s/%d.%d",
					Utils.getRandomString(7),
					Utils.getRandomInt(20, 1),
					Utils.getRandomInt(20, 1),
					Utils.getRandomInt(100, 1),
					Utils.getRandomInt(1000, 1),
					Utils.getRandomString(5),
					Utils.getRandomString(5),
					Utils.getRandomString(6),
					Utils.getRandomInt(100, 1),
					Utils.getRandomInt(100, 1),
					Utils.getRandomInt(100, 1),
					Utils.getRandomString(6),
					Utils.getRandomInt(1000, 1),
					Utils.getRandomInt(1000, 1)
			));
		}
		if (Global.config.getConfigRandomHostEnable_value()) {
			headers.removeIf(n -> (n.startsWith("Host: ")));
			headers.add("Host: " + currentRequest.getHttpService().getHost() + ".:" + Utils.getRandomInt(65535, 1));
		}
		String reqBodyStr = helpers.bytesToString(reqBody);
		if (Global.config.getConfigJson2UnicodeEnable_value()) {
			if (Utils.isJson(reqBodyStr)){
				reqBody = Utils.encodeJson2Unicode(reqBodyStr).getBytes(StandardCharsets.UTF_8);
			}
		}
		newRequest = helpers.buildHttpMessage(headers, reqBody);
		currentRequest.setRequest(newRequest);
	}
}

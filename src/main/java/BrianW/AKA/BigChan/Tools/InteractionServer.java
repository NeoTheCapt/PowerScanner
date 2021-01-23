package BrianW.AKA.BigChan.Tools;

import burp.IHttpRequestResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InteractionServer {
	private final HashMap<String, IHttpRequestResponse> requestResponseList;
	private final List<String> collaboratorPayloadList;
	
	public HashMap<String, IHttpRequestResponse> getRequestResponseList() {
		return requestResponseList;
	}
	
	public List<String> getCollaboratorPayloadList() {
		return collaboratorPayloadList;
	}
	
	public InteractionServer(){
		this.requestResponseList = new HashMap<String, IHttpRequestResponse>();
		this.collaboratorPayloadList = new ArrayList<String>();
	}
	public void addToPairList(String collaboratorPayload, IHttpRequestResponse pair){
		this.requestResponseList.put(collaboratorPayload, pair);
		this.collaboratorPayloadList.add(collaboratorPayload);
	}
	public IHttpRequestResponse findPair(String collaboratorPayload){
		return this.requestResponseList.get(collaboratorPayload);
	}
}

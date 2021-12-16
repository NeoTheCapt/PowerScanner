package BrianW.AKA.BigChan.Tools;

import burp.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class FetchCollaboratorWithSig {
    protected CustomScanIssue issue;
    protected String collaboratorPayload;
    protected String sig;
    protected IBurpExtenderCallbacks callbacks;
    protected IExtensionHelpers helpers;
    IBurpCollaboratorClientContext collaboratorContext;

    public FetchCollaboratorWithSig(CustomScanIssue issue, String collaboratorPayload, String sig, IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers,  IBurpCollaboratorClientContext collaboratorContext) {
        this.issue = issue;
        this.collaboratorPayload = collaboratorPayload;
        this.sig = sig;
        this.callbacks = callbacks;
        this.helpers = helpers;
        this.collaboratorContext = collaboratorContext;
    }

    public void start(){
        Global.fixedThreadPool.execute(this::fetch);
    }

    private void fetch() {
        try {
            Thread.sleep(Global.config.fetchCollaboratorWaitSecond * 1000);
        } catch (InterruptedException e) {
            this.callbacks.printError(String.format("Error in doScanLog4j while sleep: %s", Utils.getStackMsg(e)));
            return;
        }
        this.callbacks.printOutput(String.format("Fetch collaborator: %s; ", this.collaboratorPayload));
        List<IBurpCollaboratorInteraction> interactions = this.collaboratorContext.fetchCollaboratorInteractionsFor(this.collaboratorPayload);
        if (interactions.size() > 0) {
//            this.callbacks.printOutput(String.format("Found collaborator that match payload: %s", this.collaboratorPayload));
            for (IBurpCollaboratorInteraction interaction : interactions) {
//                this.callbacks.printOutput("checking collaborator: " + this.collaboratorPayload);
                if (interaction.getProperty("type").equalsIgnoreCase("dns") ){
                    try{
                        List<String> records = Utils.extractDnsData(Utils.Base64Decode(interaction.getProperty("raw_query").getBytes(StandardCharsets.UTF_8))).Records;
//                        this.callbacks.printOutput(String.format("%s has records: %s", this.collaboratorPayload, records));
                        if (records.contains(this.sig)){
                            this.callbacks.addScanIssue(this.issue);
                            break;
                        }
                    }catch(Exception e){
                        this.callbacks.printError(String.format("Error in FetchCollaboratorWithSig: %s", Utils.getStackMsg(e)));
                    }
                }
            }
        }
    }
}

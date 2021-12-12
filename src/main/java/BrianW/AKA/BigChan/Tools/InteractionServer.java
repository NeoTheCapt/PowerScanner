package BrianW.AKA.BigChan.Tools;

import burp.*;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class InteractionServer extends Thread {
    private final ConcurrentHashMap<String, CollaboratorData> collaboratorPayloadList;
    private final IBurpCollaboratorClientContext collaboratorContext;
    protected String threadID;
    protected IBurpExtenderCallbacks callbacks;

    public InteractionServer(IBurpExtenderCallbacks callbacks) {
        this.collaboratorPayloadList = new ConcurrentHashMap<String, CollaboratorData>();
        this.callbacks = callbacks;
        this.collaboratorContext = callbacks.createBurpCollaboratorClientContext();
        this.threadID = utils.getRandomString(5);
        this.callbacks.printOutput("InteractionServer inited, ThreadID: " + this.threadID);
    }

    @Override
    public void run() {
        try {
            checkCollaboratorPayloadList();
        } catch (Exception e) {
            callbacks.printError(utils.getStackMsg(e));
        }
        this.callbacks.printOutput("InteractionServer stopped, ThreadID: " + this.threadID);
    }

    public void addToPairList(String collaboratorPayload, CollaboratorData collaboratorData) {
        this.collaboratorPayloadList.put(collaboratorPayload, collaboratorData);
    }

    public IBurpCollaboratorClientContext getCollaboratorContext() {
        return collaboratorContext;
    }

    private void checkCollaboratorPayloadList() throws Exception {
        List<IBurpCollaboratorInteraction> interactions = new ArrayList<>();
        do {
//            this.callbacks.printOutput("CollaboratorPayloadList amount: " + collaboratorPayloadList.size());
            Iterator<ConcurrentHashMap.Entry<String, CollaboratorData>> it = this.collaboratorPayloadList.entrySet().iterator();
//            for (Map.Entry<String, CollaboratorData> collaboratorPayload : this.collaboratorPayloadList.entrySet()) {
            while (it.hasNext()){
                ConcurrentHashMap.Entry<String, CollaboratorData> collaboratorPayload = it.next();
                this.callbacks.printOutput("checking collaborator: " + collaboratorPayload.getValue() + ", key: " + collaboratorPayload.getKey());
                interactions = this.collaboratorContext.fetchCollaboratorInteractionsFor(collaboratorPayload.getKey());
                if (interactions.size() > 0) {
                    if (collaboratorPayload.getValue().sig.length()>0){
                        for (IBurpCollaboratorInteraction interaction : interactions) {
                            if (interaction.getProperty("type").equalsIgnoreCase("dns") ){
                                try{
                                    this.callbacks.printOutput("checking collaborator: " + collaboratorPayload + ", records: " + utils.extractDnsData(utils.Base64Decode(interaction.getProperty("raw_query").getBytes(StandardCharsets.UTF_8))).Records);
                                    if (utils.extractDnsData(utils.Base64Decode(interaction.getProperty("raw_query").getBytes(StandardCharsets.UTF_8))).Records.contains(collaboratorPayload.getValue().sig)){
                                        this.callbacks.addScanIssue(collaboratorPayload.getValue().issue);
//                                    this.callbacks.printOutput("remove issue1 collaborator: " + collaboratorPayload + ", ThreadID: " + this.threadID);
//                                    this.collaboratorPayloadList.remove(collaboratorPayload.getKey());
                                        it.remove();
                                        break;
                                    }
                                }catch(Exception e){
                                    callbacks.printError(String.format("Error in processProxyMessage: %s", utils.getStackMsg(e)));
                                }
                            }
                        }
                    }else{
                        this.callbacks.addScanIssue(collaboratorPayload.getValue().issue);
//                        this.callbacks.printOutput("remove issue2 collaborator: " + collaboratorPayload + ", ThreadID: " + this.threadID);
                        it.remove();
                    }
                    continue;
                }
                if ((new Date()).getTime() - collaboratorPayload.getValue().cdate.getTime() > 10 * 1000) {
//                    this.callbacks.printOutput("remove timeout collaborator: " + collaboratorPayload + ", ThreadID: " + this.threadID);
                    it.remove();
                }
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                break;//捕获到异常之后，执行break跳出循环。
            }
        } while (!this.isInterrupted());
    }
}

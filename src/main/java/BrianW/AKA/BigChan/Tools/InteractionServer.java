package BrianW.AKA.BigChan.Tools;

import burp.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class InteractionServer extends Thread {
    private final List<String> collaboratorPayloadList;
    private ConcurrentHashMap<String, IScanIssue> issuesList;
    private ConcurrentHashMap<String, Date> dateList;
    private IBurpCollaboratorClientContext collaboratorContext;
    protected String threadID;
    protected IBurpExtenderCallbacks callbacks;

    public InteractionServer(IBurpExtenderCallbacks callbacks) {
        this.issuesList = new ConcurrentHashMap<String, IScanIssue>();
        this.collaboratorPayloadList = new CopyOnWriteArrayList<String>();
        this.dateList = new ConcurrentHashMap<String, Date>();
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

    public void addToPairList(String collaboratorPayload, IScanIssue issue) {
        this.collaboratorPayloadList.add(collaboratorPayload);
        this.issuesList.put(collaboratorPayload, issue);
        this.dateList.put(collaboratorPayload, new Date());
    }

    public IBurpCollaboratorClientContext getCollaboratorContext() {
        return collaboratorContext;
    }

    private void checkCollaboratorPayloadList() throws Exception {
        List<IBurpCollaboratorInteraction> interactions = new ArrayList<>();
        do {
            for (String collaboratorPayload : this.collaboratorPayloadList) {
                //this.callbacks.printOutput("checking collaborator: " + collaboratorPayload + ", ThreadID: " + this.threadID);
                interactions = this.collaboratorContext.fetchCollaboratorInteractionsFor(collaboratorPayload);
                if (interactions.size() > 0) {
                    this.callbacks.addScanIssue(this.issuesList.get(collaboratorPayload));
                    this.collaboratorPayloadList.remove(collaboratorPayload);
                    this.issuesList.remove(collaboratorPayload);
                    this.dateList.remove(collaboratorPayload);
                    continue;
                }
                if ((new Date()).getTime() - this.dateList.get(collaboratorPayload).getTime() > 10 * 1000) {
                    this.collaboratorPayloadList.remove(collaboratorPayload);
                    this.issuesList.remove(collaboratorPayload);
                    this.dateList.remove(collaboratorPayload);
                    //this.callbacks.printOutput("remove collaborator: " + collaboratorPayload + ", ThreadID: " + this.threadID);
                }
            }
            try{
                sleep(3000);
            }catch(InterruptedException e){
            break;//捕获到异常之后，执行break跳出循环。
        }
    } while (!this.isInterrupted());
    }
}

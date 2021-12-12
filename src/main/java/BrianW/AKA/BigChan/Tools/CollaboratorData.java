package BrianW.AKA.BigChan.Tools;

import burp.IScanIssue;

import java.util.Date;

public class CollaboratorData {
    public IScanIssue issue;
    public Date cdate;
    public String sig;

    public CollaboratorData(IScanIssue issue, Date cdate, String sig) {
        this.issue = issue;
        this.cdate = cdate;
        this.sig = sig;
    }
}

package BrianW.AKA.BigChan.Handlers;

import BrianW.AKA.BigChan.PowerScanner.*;
import BrianW.AKA.BigChan.Tools.Global;
import BrianW.AKA.BigChan.Tools.InteractionServer;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

import java.util.ArrayList;
import java.util.List;

public class PerRequestHandler implements IScannerCheck {
    private final IBurpExtenderCallbacks callbacks;
    private final IExtensionHelpers helpers;

    public PerRequestHandler(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
        this.callbacks = callbacks;
        this.helpers = helpers;
    }

    @Override
    public List<IScanIssue> doPassiveScan(IHttpRequestResponse baseRequestResponse) {
        List<IScanIssue> issues = new ArrayList<>();
        if (Global.config.getConfigSensitiveParamEnable_value()) {
            issues.addAll(
                    new ScanSensitiveParam(callbacks, helpers).doScanSensitiveParam(baseRequestResponse)
            );
        }
//        issues.addAll(
//                new ScanSensitiveInfo(callbacks, helpers).doScanSensitiveInfo(baseRequestResponse)
//        );
        List nullList = new ArrayList();
        nullList.add(null);
        issues.removeAll(nullList);
        return issues;
    }

    @Override
    public List<IScanIssue> doActiveScan(IHttpRequestResponse baseRequestResponse, IScannerInsertionPoint insertionPoint) {
        // report the issue
        List<IScanIssue> issues = new ArrayList<>();
        if (Global.config.getConfigSqliEnable_value()) {
            issues.add(
                    new ScanSqli(callbacks, helpers).doScanSqli(baseRequestResponse, insertionPoint)
            );
        }
        if (Global.config.getConfigRCEEnable_value()) {
            issues.add(
                    new ScanRCE(callbacks, helpers).doScanRCE(baseRequestResponse, insertionPoint)
            );
        }
        if (Global.config.getConfigPathTraversalEnable_value()) {
            issues.add(
                    new ScanPathTraversal(callbacks, helpers).doScanPathTraversal(baseRequestResponse, insertionPoint)
            );
        }
        if (Global.config.getConfigFastjsonEnable_value()) {
            issues.add(
                    new ScanFastJson(callbacks, helpers).doScanFastJson(baseRequestResponse, insertionPoint)
            );
        }
        if (Global.config.getConfigLog4jEnable_value()) {
            issues.add(
                    new ScanLog4j(callbacks, helpers).doScanLog4j(baseRequestResponse, insertionPoint)
            );
        }
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


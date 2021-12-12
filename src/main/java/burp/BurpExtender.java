package burp;

import BrianW.AKA.BigChan.GUI.PowerTab;
import BrianW.AKA.BigChan.Handlers.*;
import BrianW.AKA.BigChan.Tools.Config;
import BrianW.AKA.BigChan.Tools.Global;

import java.util.List;

public class BurpExtender implements IBurpExtender {
    @Override
    public void registerExtenderCallbacks(final IBurpExtenderCallbacks callbacks) {
        // keep a reference to our callbacks object
        //
        // implement IBurpExtender
        //
        // obtain an extension helpers object

        Global.config = new Config(callbacks);
        Global.PowerTab = new PowerTab(callbacks);
        callbacks.addSuiteTab(Global.PowerTab);
        IExtensionHelpers helpers = callbacks.getHelpers();
        // set our extension name
        callbacks.setExtensionName("PowerScanner by Brian.W");
        // register ourselves as a custom scanner check
        IScannerCheck PerRequestScans = new PerRequestHandler(callbacks, helpers);
        IScannerCheck PerHostScans = new PerHostHandler(callbacks, helpers);
        ISessionHandlingAction SessionHandler = new SessionHandler(callbacks, helpers);
        IExtensionStateListener StateHandler = new StateHandler(callbacks, helpers);
        IProxyListener requesthandler = new RequestHandler(callbacks, helpers);
        callbacks.registerScannerCheck(PerRequestScans);
        callbacks.registerScannerCheck(PerHostScans);
        callbacks.registerSessionHandlingAction(SessionHandler);
        callbacks.registerExtensionStateListener(StateHandler);
        callbacks.registerProxyListener(requesthandler);
        callbacks.printOutput("PowerScanner by Brian.W");
        callbacks.printOutput("Start scanner!");
//		List<IScannerCheck> ScannerChecks = callbacks.getScannerChecks();
//		for (IScannerCheck ScannerCheck: ScannerChecks){
//			callbacks.printOutput(ScannerCheck.toString());
//		}
    }
}
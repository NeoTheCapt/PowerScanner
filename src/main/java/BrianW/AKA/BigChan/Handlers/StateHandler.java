package BrianW.AKA.BigChan.Handlers;

import BrianW.AKA.BigChan.Tools.Global;
import burp.IBurpExtenderCallbacks;
import burp.IExtensionHelpers;
import burp.IExtensionStateListener;

public class StateHandler implements IExtensionStateListener {
    protected IBurpExtenderCallbacks callbacks;
    protected IExtensionHelpers helpers;
    public StateHandler(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
        this.callbacks = callbacks;
        this.helpers = helpers;
    }

    @Override
    public void extensionUnloaded() {
//        Global.interactionServer.interrupt();
        this.callbacks.printOutput("Extension unloaded");
    }
}

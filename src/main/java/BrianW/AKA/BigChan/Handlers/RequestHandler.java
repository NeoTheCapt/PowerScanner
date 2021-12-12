package BrianW.AKA.BigChan.Handlers;

import BrianW.AKA.BigChan.Tools.Global;
import BrianW.AKA.BigChan.Tools.RequestHelper;
import BrianW.AKA.BigChan.Tools.SendToProxy;
import BrianW.AKA.BigChan.Tools.utils;
import burp.*;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.List;

public class RequestHandler implements IProxyListener{
    protected IBurpExtenderCallbacks callbacks;
    protected IExtensionHelpers helpers;
    public void IProxyListener(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
        this.callbacks = callbacks;
        this.helpers = helpers;
    }

    public RequestHandler(IBurpExtenderCallbacks callbacks, IExtensionHelpers helpers) {
        this.callbacks = callbacks;
        this.helpers = helpers;
    }

    @Override
    public void processProxyMessage(boolean messageIsRequest, IInterceptedProxyMessage message) {
        if (messageIsRequest && Global.config.getConfigRequestRouteEnable_value()){
            String[] proxyList = Global.config.getConfigRequestRoute_value().split("\\r?\\n");
            for (String proxyStr : proxyList) {
                Proxy proxy = new Proxy(
                        Proxy.Type.HTTP,
                        new InetSocketAddress(proxyStr.split(":")[0],
                                Integer.parseInt(proxyStr.split(":")[1]))
                );
                byte[] currentRequest = message.getMessageInfo().getRequest();
                IHttpService service = message.getMessageInfo().getHttpService();
                String host = service.getHost();
                int port = message.getMessageInfo().getHttpService().getPort();
                String protocol = message.getMessageInfo().getHttpService().getProtocol();
                String method = this.helpers.analyzeRequest(message.getMessageInfo()).getMethod();
                URL url = this.helpers.analyzeRequest(message.getMessageInfo()).getUrl();
                List<IParameter> parameters = this.helpers.analyzeRequest(message.getMessageInfo()).getParameters();
                List<String> headers = this.helpers.analyzeRequest(message.getMessageInfo()).getHeaders();
                int bodySize = currentRequest.length - this.helpers.analyzeRequest(currentRequest).getBodyOffset();
                byte[] reqBody = new byte[bodySize];
                System.arraycopy(currentRequest, this.helpers.analyzeRequest(currentRequest).getBodyOffset(), reqBody, 0, bodySize);
                callbacks.printOutput(headers.get(0));
                byte[] bytes = new byte[0];
                SendToProxy sendToProxy = new SendToProxy(url, headers, reqBody, proxy);
                sendToProxy.start();
            }

        }

    }
}

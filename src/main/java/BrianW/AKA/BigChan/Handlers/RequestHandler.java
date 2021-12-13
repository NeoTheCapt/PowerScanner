package BrianW.AKA.BigChan.Handlers;

import BrianW.AKA.BigChan.Tools.Global;
import BrianW.AKA.BigChan.Tools.SendToProxy;
import burp.*;
import org.apache.http.HttpHost;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Arrays;
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
                String user = "";
                String pass = "";
                if (proxyStr.contains("@")){
                    String acc = proxyStr.split("@")[0];
                    if (acc.contains(":")){
                        user = acc.split(":")[0];
                        pass = acc.split(":")[1];
                        proxyStr = proxyStr.split("@")[1];
                    }
                }
                HttpHost proxy = new HttpHost(proxyStr.split(":")[0], Integer.parseInt(proxyStr.split(":")[1]));
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
//                callbacks.printOutput(String.format("body size=%s, body = %s", bodySize, Arrays.toString(reqBody)));
                byte[] bytes = new byte[0];
//                SendToProxy sendToProxy = new SendToProxy(url, method, headers, reqBody, proxy, user, pass);
//                sendToProxy.run();
                Thread sendToProxy = new Thread(new SendToProxy(url, method, headers, reqBody, proxy, user, pass));
                sendToProxy.start();
            }
        }
    }
}

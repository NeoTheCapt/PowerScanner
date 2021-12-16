package BrianW.AKA.BigChan.Tools;

import org.apache.http.HttpHost;
import java.net.URL;
import java.util.List;

public class SendToProxy implements Runnable{
    protected URL url;
    protected List<String> headers;
    protected byte[] body;
    protected HttpHost proxy;
    protected String user;
    protected String pass;
    protected String method;
    public SendToProxy(URL url, String method, List<String> headers, byte[] body, HttpHost proxy){
        this.url = url;
        this.headers = headers;
        this.body = body;
        this.proxy = proxy;
        this.user = "";
        this.pass = "";
        this.method = method;
    }
    public SendToProxy(URL url, String method, List<String> headers, byte[] body, HttpHost proxy, String user, String pass){
        this.url = url;
        this.headers = headers;
        this.body = body;
        this.proxy = proxy;
        this.user = user;
        this.pass = pass;
        this.method = method;
    }

    public void run() {
        try {
            RequestHelper.doRequestViaProxy(this.url, this.method, this.headers, this.body, this.proxy, this.user, this.pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

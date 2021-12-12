package BrianW.AKA.BigChan.Tools;

import java.net.Proxy;
import java.net.URL;
import java.util.List;

public class SendToProxy extends Thread{
    public URL url;
    public List<String> headers;
    public byte[] body;
    public Proxy proxy;
    public SendToProxy(URL url, List<String> headers, byte[] body, Proxy proxy){
        this.url = url;
        this.headers = headers;
        this.body = body;
        this.proxy = proxy;
    }
    @Override
    public void run() {
        try {
            byte[] bytes = RequestHelper.doRequestViaProxy(this.url, this.headers, this.body, this.proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

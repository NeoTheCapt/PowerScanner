package BrianW.AKA.BigChan.Tools;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProxiedHttpsConnection extends HttpURLConnection {

    private final String proxyHost;
    private final int proxyPort;
    private static final byte[] NEWLINE = "\r\n".getBytes();//should be "ASCII7"

    private Socket socket;
    private final Map<String, List<String>> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final Map<String, List<String>> sendheaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final Map<String, List<String>> proxyheaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final Map<String, List<String>> proxyreturnheaders = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private int statusCode;
    private String statusLine;
    private boolean isDoneWriting;

    public ProxiedHttpsConnection(URL url,
                                  String proxyHost, int proxyPort, String username, String password)
            throws IOException {
        super(url);
        socket = new Socket();
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        String encoded = Base64.getEncoder().encodeToString((username + ":" + password).getBytes())
                .replace("\r\n", "");
        proxyheaders.put("Proxy-Authorization", new ArrayList<>(Arrays.asList("Basic " + encoded)));
    }

    @Override
    public FilterOutputStream getOutputStream() throws IOException {
        connect();
        afterWrite();
        return new FilterOutputStream(socket.getOutputStream()) {
            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                out.write(String.valueOf(len).getBytes());
                out.write(NEWLINE);
                out.write(b, off, len);
                out.write(NEWLINE);
            }

            @Override
            public void write(byte[] b) throws IOException {
                out.write(String.valueOf(b.length).getBytes());
                out.write(NEWLINE);
                out.write(b);
                out.write(NEWLINE);
            }

            @Override
            public void write(int b) throws IOException {
                out.write(String.valueOf(1).getBytes());
                out.write(NEWLINE);
                out.write(b);
                out.write(NEWLINE);
            }

            @Override
            public void close() throws IOException {
                afterWrite();
            }

        };
    }

    private boolean afterwritten = false;

    @Override
    public InputStream getInputStream() throws IOException {
        connect();
        return socket.getInputStream();

    }

    @Override
    public void setRequestMethod(String method) throws ProtocolException {
        this.method = method;
    }

    @Override
    public void setRequestProperty(String key, String value) {
        sendheaders.put(key, new ArrayList<>(Arrays.asList(value)));
    }

    @Override
    public void addRequestProperty(String key, String value) {
        sendheaders.computeIfAbsent(key, l -> new ArrayList<>()).add(value);
    }

    @Override
    public Map<String, List<String>> getHeaderFields() {
        return headers;
    }

    @Override
    public void connect() throws IOException {
        if (connected) {
            return;
        }
        connected = true;
        socket.setSoTimeout(getReadTimeout());
        socket.connect(new InetSocketAddress(proxyHost, proxyPort), getConnectTimeout());
        StringBuilder msg = new StringBuilder();
        msg.append("CONNECT ");
        msg.append(url.getHost());
        msg.append(':');
        msg.append(url.getPort() == -1 ? 443 : url.getPort());
        msg.append(" HTTP/1.0\r\n");
        for (Map.Entry<String, List<String>> header : proxyheaders.entrySet()) {
            for (String l : header.getValue()) {
                msg.append(header.getKey()).append(": ").append(l);
                msg.append("\r\n");
            }
        }

        msg.append("Connection: close\r\n");
        msg.append("\r\n");
        byte[] bytes;
        try {
            bytes = msg.toString().getBytes("ASCII7");
        } catch (UnsupportedEncodingException ignored) {
            bytes = msg.toString().getBytes();
        }
        socket.getOutputStream().write(bytes);
        socket.getOutputStream().flush();
        byte reply[] = new byte[200];
        byte header[] = new byte[200];
        int replyLen = 0;
        int headerLen = 0;
        int newlinesSeen = 0;
        boolean headerDone = false;
        /* Done on first newline */
        InputStream in = socket.getInputStream();
        while (newlinesSeen < 2) {
            int i = in.read();
            if (i < 0) {
                throw new IOException("Unexpected EOF from remote server");
            }
            if (i == '\n') {
                if (newlinesSeen != 0) {
                    String h = new String(header, 0, headerLen);
                    String[] split = h.split(": ");
                    if (split.length != 1) {
                        proxyreturnheaders.computeIfAbsent(split[0], l -> new ArrayList<>()).add(split[1]);
                    }
                }
                headerDone = true;
                ++newlinesSeen;
                headerLen = 0;
            } else if (i != '\r') {
                newlinesSeen = 0;
                if (!headerDone && replyLen < reply.length) {
                    reply[replyLen++] = (byte) i;
                } else if (headerLen < reply.length) {
                    header[headerLen++] = (byte) i;
                }
            }
        }

        String replyStr;
        try {
            replyStr = new String(reply, 0, replyLen, "ASCII7");
        } catch (UnsupportedEncodingException ignored) {
            replyStr = new String(reply, 0, replyLen);
        }

        // Some proxies return http/1.1, some http/1.0 even we asked for 1.0
        if (!replyStr.startsWith("HTTP/1.0 200") && !replyStr.startsWith("HTTP/1.1 200")) {
            throw new IOException("Unable to tunnel. Proxy returns \"" + replyStr + "\"");
        }
        SSLSocket s = (SSLSocket) ((SSLSocketFactory) SSLSocketFactory.getDefault())
                .createSocket(socket, url.getHost(), url.getPort(), true);
        s.startHandshake();
        socket = s;
        msg.setLength(0);
        msg.append(method);
        msg.append(" ");
        msg.append(url.toExternalForm().split(String.valueOf(url.getPort()), -2)[1]);
        msg.append(" HTTP/1.0\r\n");
        for (Map.Entry<String, List<String>> h : sendheaders.entrySet()) {
            for (String l : h.getValue()) {
                msg.append(h.getKey()).append(": ").append(l);
                msg.append("\r\n");
            }
        }
        if (method.equals("POST") || method.equals("PUT")) {
            msg.append("Transfer-Encoding: Chunked\r\n");
        }
        msg.append("Host: ").append(url.getHost()).append("\r\n");
        msg.append("Connection: close\r\n");
        msg.append("\r\n");
        try {
            bytes = msg.toString().getBytes("ASCII7");
        } catch (UnsupportedEncodingException ignored) {
            bytes = msg.toString().getBytes();
        }
        socket.getOutputStream().write(bytes);
        socket.getOutputStream().flush();
    }

    private void afterWrite() throws IOException {
        if (afterwritten) {
            return;
        }
        afterwritten = true;
        socket.getOutputStream().write(String.valueOf(0).getBytes());
        socket.getOutputStream().write(NEWLINE);
        socket.getOutputStream().write(NEWLINE);
        byte reply[] = new byte[200];
        byte header[] = new byte[200];
        int replyLen = 0;
        int headerLen = 0;
        int newlinesSeen = 0;
        boolean headerDone = false;
        /* Done on first newline */
        InputStream in = socket.getInputStream();
        while (newlinesSeen < 2) {
            int i = in.read();
            if (i < 0) {
                throw new IOException("Unexpected EOF from remote server");
            }
            if (i == '\n') {
                if (headerDone) {
                    String h = new String(header, 0, headerLen);
                    String[] split = h.split(": ");
                    if (split.length != 1) {
                        headers.computeIfAbsent(split[0], l -> new ArrayList<>()).add(split[1]);
                    }
                }
                headerDone = true;
                ++newlinesSeen;
                headerLen = 0;
            } else if (i != '\r') {
                newlinesSeen = 0;
                if (!headerDone && replyLen < reply.length) {
                    reply[replyLen++] = (byte) i;
                } else if (headerLen < header.length) {
                    header[headerLen++] = (byte) i;
                }
            }
        }

        String replyStr;
        try {
            replyStr = new String(reply, 0, replyLen, "ASCII7");
        } catch (UnsupportedEncodingException ignored) {
            replyStr = new String(reply, 0, replyLen);
        }

        /* We asked for HTTP/1.0, so we should get that back */
        if ((!replyStr.startsWith("HTTP/1.0 200")) && !replyStr.startsWith("HTTP/1.1 200")) {
            throw new IOException("Server returns \"" + replyStr + "\"");
        }
    }

    @Override
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ProxiedHttpsConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean usingProxy() {
        return true;
    }
}
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;
import java.util.*;

public class WebRTCSignalingServer extends WebSocketServer {

    private final Set<WebSocket> clients = new HashSet<>();

    public WebRTCSignalingServer(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);
        System.out.println("New connection from " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
        System.out.println("Connection closed: " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        for (WebSocket client : clients) {
            if (!client.equals(conn)) {
                client.send(message);
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.println("WebRTC signaling server started on port " + getPort());
    }

    public static void main(String[] args) {
        WebRTCSignalingServer server = new WebRTCSignalingServer(8887);
        server.start();
    }
}


import java.io.*;
import java.net.*;
import java.util.*;

public class DHCPServer {
    private static final int SERVER_PORT = 6767; 
    private static final String[] IP_POOL = {
        "192.168.1.10", "192.168.1.11", "192.168.1.12", "192.168.1.13"
    };
    private static final Map<String, String> allocatedIPs = new HashMap<>();

    public static void main(String[] args) throws IOException {
        DatagramSocket serverSocket = new DatagramSocket(SERVER_PORT);
        byte[] buffer = new byte[1024];
        System.out.println("DHCP Server is running on port " + SERVER_PORT);

        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(packet);

            String message = new String(packet.getData(), 0, packet.getLength());
            InetAddress clientAddress = packet.getAddress();
            int clientPort = packet.getPort();
            System.out.println("Received: " + message + " from " + clientAddress);

            String response = "";

            if (message.equals("DISCOVER")) {
                String offeredIp = getAvailableIP(clientAddress.getHostAddress());
                response = "OFFER:" + offeredIp;
            } else if (message.startsWith("REQUEST:")) {
                String requestedIp = message.split(":")[1];
                boolean success = assignIP(clientAddress.getHostAddress(), requestedIp);
                response = success ? "ACK:" + requestedIp : "NAK";
            }

            byte[] sendData = response.getBytes();
            DatagramPacket responsePacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
            serverSocket.send(responsePacket);
        }
    }

    private static String getAvailableIP(String clientId) {
        for (String ip : IP_POOL) {
            if (!allocatedIPs.containsValue(ip)) {
                return ip;
            }
        }
        return "0.0.0.0";
    }

    private static boolean assignIP(String clientId, String ip) {
        if (allocatedIPs.containsValue(ip)) return false;
        allocatedIPs.put(clientId, ip);
        return true;
    }
}


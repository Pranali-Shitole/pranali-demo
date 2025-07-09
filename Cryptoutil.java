import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class CryptoUtil {
    public static SecretKey generateAESKey() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        return generator.generateKey();
    }

    public static String encrypt(String plainText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] enc = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(enc);
    }

    public static String decrypt(String cipherText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] dec = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(dec);
    }
}

public class Node {
    private static final Map<Integer, SecretKey> keys = new HashMap<>(); // NodeID → Key

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        int nodeId = Integer.parseInt(args[1]);
        SecretKey key = CryptoUtil.generateAESKey();
        keys.put(nodeId, key);

        ServerSocket server = new ServerSocket(port);
        System.out.println("Node " + nodeId + " listening on port " + port);

        while (true) {
            Socket client = server.accept();
            new Thread(() -> handleClient(client, nodeId)).start();
        }
    }

    private static void handleClient(Socket socket, int nodeId) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String encrypted = in.readLine();
            String decrypted = CryptoUtil.decrypt(encrypted, keys.get(nodeId));
            System.out.println("Node " + nodeId + " received: " + decrypted);

            String[] parts = decrypted.split(":", 3); 
            if (parts.length == 3) {
                String nextHost = parts[0];
                int nextPort = Integer.parseInt(parts[1]);
                String nextPayload = parts[2];

                Socket forwardSocket = new Socket(nextHost, nextPort);
                PrintWriter forwardOut = new PrintWriter(forwardSocket.getOutputStream(), true);
                forwardOut.println(nextPayload);
                forwardSocket.close();
            } else {
                System.out.println("Exit node message: " + decrypted);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SecretKey getKey(int id) {
        return keys.get(id);
    }
}

public class Client {
    public static void main(String[] args) throws Exception {
        List<NodeInfo> path = new ArrayList<>();
        path.add(new NodeInfo("localhost", 9001, 1));
        path.add(new NodeInfo("localhost", 9002, 2));
        path.add(new NodeInfo("localhost", 9003, 3)); 
        Map<Integer, SecretKey> keys = new HashMap<>();
        for (NodeInfo node : path) {
            keys.put(node.id, CryptoUtil.generateAESKey());
        }
        String message = "Hello from client!";
        for (int i = path.size() - 1; i >= 0; i--) {
            NodeInfo node = path.get(i);
            if (i == path.size() - 1) {
                message = message;
            } else {
                NodeInfo next = path.get(i + 1);
                message = next.host + ":" + next.port + ":" + message;
            }
            message = CryptoUtil.encrypt(message, keys.get(node.id));
        }
        NodeInfo first = path.get(0);
        try (Socket socket = new Socket(first.host, first.port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            out.println(message);
            System.out.println("Sent encrypted onion message.");
        }
    }

    static class NodeInfo {
        String host;
        int port;
        int id;

        NodeInfo(String host, int port, int id) {
            this.host = host;
            this.port = port;
            this.id = id;
        }
    }
}


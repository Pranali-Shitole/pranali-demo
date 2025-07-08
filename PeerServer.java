import java.io.*;
import java.net.*;

public class PeerServer {
    private static final int PORT = 9876; 

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Peer server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream out = socket.getOutputStream()
        ) {
            String fileName = in.readLine();  
            File file = new File(fileName);

            if (file.exists()) {
                byte[] buffer = new byte[4096];
                FileInputStream fis = new FileInputStream(file);
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
                fis.close();
                System.out.println("Sent file: " + fileName);
            } else {
                PrintWriter pw = new PrintWriter(out, true);
                pw.println("ERROR: File not found");
                System.out.println("Requested file not found: " + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class PeerClient {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java PeerClient <server-ip> <port> <file-name>");
            return;
        }

        String serverIp = args[0];
        int port = Integer.parseInt(args[1]);
        String fileName = args[2];

        try (
            Socket socket = new Socket(serverIp, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            InputStream in = socket.getInputStream()
        ) {
            out.println(fileName); 

            FileOutputStream fos = new FileOutputStream("received_" + fileName);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = in.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            fos.close();
            System.out.println("File downloaded as: received_" + fileName);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}


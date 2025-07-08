import java.io.*;
import java.net.*;

public class HTTPProxyServer {
    public static void main(String[] args) {
        int port = 8888; 

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Proxy server is running on port " + port + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            InputStream clientIn = clientSocket.getInputStream();
            OutputStream clientOut = clientSocket.getOutputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientIn))
        ) {
            String requestLine = reader.readLine();
            if (requestLine == null || !requestLine.startsWith("GET")) {
                clientSocket.close();
                return;
            }
            System.out.println("Request: " + requestLine);

            String[] parts = requestLine.split(" ");
            if (parts.length < 2) return;
            URL url = new URL(parts[1]);

            String host = url.getHost();
            int port = url.getPort() == -1 ? 80 : url.getPort();
            Socket serverSocket = new Socket(host, port);
            PrintWriter serverOut = new PrintWriter(serverSocket.getOutputStream(), true);
            BufferedReader serverIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            serverOut.println("GET " + url.getFile() + " HTTP/1.0");
            String header;
            while (!(header = reader.readLine()).isEmpty()) {
                serverOut.println(header);
            }
            serverOut.println(); 
            String responseLine;
            BufferedWriter clientWriter = new BufferedWriter(new OutputStreamWriter(clientOut));
            while ((responseLine = serverIn.readLine()) != null) {
                clientWriter.write(responseLine + "\r\n");
                clientWriter.flush();
            }

            serverSocket.close();
            clientSocket.close();

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}


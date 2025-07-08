import java.io.*;
import java.net.*;

public class ReverseProxyServer {
    private static final int PROXY_PORT = 8080;
    private static final String BACKEND_HOST = "localhost";
    private static final int BACKEND_PORT = 8000;

    public static void main(String[] args) {
        try (ServerSocket proxySocket = new ServerSocket(PROXY_PORT)) {
            System.out.println("Reverse Proxy running on port " + PROXY_PORT);

            while (true) {
                Socket clientSocket = proxySocket.accept();
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
            Socket backendSocket = new Socket(BACKEND_HOST, BACKEND_PORT);
            InputStream backendIn = backendSocket.getInputStream();
            OutputStream backendOut = backendSocket.getOutputStream()
        ) {
            forwardData(clientIn, backendOut);
            forwardData(backendIn, clientOut);

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException ignored) {}
        }
    }

    private static void forwardData(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead;

        while (in.available() > 0 || (bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            if (in.available() == 0) break;
        }
        out.flush();
    }
}


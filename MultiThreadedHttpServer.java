import java.io.*;
import java.net.*;

public class MultiThreadedHttpServer {

    private static final int PORT = 8080;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("HTTP Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            System.err.println("Server exception: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()))
            ) {
                String requestLine = in.readLine();
                if (requestLine == null || requestLine.isEmpty()) {
                    clientSocket.close();
                    return;
                }

                System.out.println("Received request: " + requestLine);
                String[] tokens = requestLine.split(" ");
                if (tokens.length < 3) {
                    sendBadRequest(out);
                    return;
                }

                String method = tokens[0];
                String path = tokens[1];

                if (!method.equals("GET")) {
                    sendMethodNotAllowed(out);
                    return;
                }
                String responseBody = "<html><body><h1>Welcome to the Multi-threaded HTTP Server!</h1>" +
                                      "<p>You requested: " + path + "</p></body></html>";

                sendOkResponse(out, responseBody);

            } catch (IOException e) {
                System.err.println("Client handler error: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException ignored) {}
            }
        }

        private void sendOkResponse(BufferedWriter out, String body) throws IOException {
            out.write("HTTP/1.1 200 OK\r\n");
            out.write("Content-Type: text/html; charset=UTF-8\r\n");
            out.write("Content-Length: " + body.getBytes().length + "\r\n");
            out.write("Connection: close\r\n");
            out.write("\r\n");
            out.write(body);
            out.flush();
        }

        private void sendBadRequest(BufferedWriter out) throws IOException {
            String body = "<html><body><h1>400 Bad Request</h1></body></html>";
            out.write("HTTP/1.1 400 Bad Request\r\n");
            out.write("Content-Type: text/html; charset=UTF-8\r\n");
            out.write("Content-Length: " + body.getBytes().length + "\r\n");
            out.write("Connection: close\r\n");
            out.write("\r\n");
            out.write(body);
            out.flush();
        }

        private void sendMethodNotAllowed(BufferedWriter out) throws IOException {
            String body = "<html><body><h1>405 Method Not Allowed</h1></body></html>";
            out.write("HTTP/1.1 405 Method Not Allowed\r\n");
            out.write("Content-Type: text/html; charset=UTF-8\r\n");
            out.write("Content-Length: " + body.getBytes().length + "\r\n");
            out.write("Connection: close\r\n");
            out.write("\r\n");
            out.write(body);
            out.flush();
        }
    }
}


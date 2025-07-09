import java.io.*;
import java.net.*;

public class RemoteShellServer {
    public static void main(String[] args) throws IOException {
        int port = 5000;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Remote Shell Server started on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected: " + clientSocket.getInetAddress());
            new Thread(new ClientHandler(clientSocket)).start();
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
            ) {
                String command;
                while ((command = in.readLine()) != null) {
                    if (command.equalsIgnoreCase("exit")) {
                        break;
                    }
                    String result = executeCommand(command);
                    out.println(result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String executeCommand(String command) {
            StringBuilder output = new StringBuilder();
            try {
                ProcessBuilder builder = new ProcessBuilder();
                if (System.getProperty("os.name").toLowerCase().contains("win")) {
                    builder.command("cmd.exe", "/c", command);
                } else {
                    builder.command("sh", "-c", command);
                }
                builder.redirectErrorStream(true);
                Process process = builder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
                process.waitFor();
            } catch (Exception e) {
                output.append("Error executing command: ").append(e.getMessage());
            }
            return output.toString();
        }
    }
}

public class RemoteShellClient {
    public static void main(String[] args) throws IOException {
        String host = "localhost";
        int port = 5000;

        Socket socket = new Socket(host, port);
        System.out.println("Connected to Remote Shell Server");

        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        String input;
        while (true) {
            System.out.print("RemoteShell> ");
            input = userInput.readLine();
            if (input == null || input.equalsIgnoreCase("exit")) {
                out.println("exit");
                break;
            }
            out.println(input);
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                System.out.println(line);
            }
        }

        socket.close();
    }
}


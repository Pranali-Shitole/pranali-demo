import java.io.*;
import java.net.*;

public class FTPServer {
    private static final int PORT = 2121;
    private static final String SERVER_DIR = "server_files";

    public static void main(String[] args) {
        File dir = new File(SERVER_DIR);
        if (!dir.exists()) dir.mkdir();

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("FTP Server started on port " + PORT);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected.");

                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream())
        ) {
            while (true) {
                String command = in.readUTF();
                System.out.println("Received: " + command);

                switch (command) {
                    case "LIST":
                        File folder = new File(SERVER_DIR);
                        String[] files = folder.list();
                        out.writeInt(files.length);
                        for (String file : files) {
                            out.writeUTF(file);
                        }
                        break;

                    case "UPLOAD":
                        String uploadName = in.readUTF();
                        long size = in.readLong();
                        try (FileOutputStream fos = new FileOutputStream(SERVER_DIR + "/" + uploadName)) {
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            long remaining = size;
                            while ((bytesRead = in.read(buffer, 0, (int)Math.min(buffer.length, remaining))) > 0) {
                                fos.write(buffer, 0, bytesRead);
                                remaining -= bytesRead;
                                if (remaining == 0) break;
                            }
                        }
                        out.writeUTF("UPLOAD_OK");
                        break;

                    case "DOWNLOAD":
                        String downloadName = in.readUTF();
                        File file = new File(SERVER_DIR + "/" + downloadName);
                        if (file.exists()) {
                            out.writeUTF("FOUND");
                            out.writeLong(file.length());

                            try (FileInputStream fis = new FileInputStream(file)) {
                                byte[] buffer = new byte[4096];
                                int bytesRead;
                                while ((bytesRead = fis.read(buffer)) > 0) {
                                    out.write(buffer, 0, bytesRead);
                                }
                            }
                        } else {
                            out.writeUTF("NOT_FOUND");
                        }
                        break;

                    case "QUIT":
                        socket.close();
                        return;
                }
            }
        } catch (IOException ex) {
            System.out.println("Client disconnected.");
        }
    }

public class FTPClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 2121;

    public static void main(String[] args) {
        try (
            Socket socket = new Socket(SERVER_IP, PORT);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to FTP server.");

            while (true) {
                System.out.print("\nEnter command (LIST, UPLOAD, DOWNLOAD, QUIT): ");
                String command = scanner.nextLine().trim().toUpperCase();
                out.writeUTF(command);

                switch (command) {
                    case "LIST":
                        int count = in.readInt();
                        System.out.println("Files on server:");
                        for (int i = 0; i < count; i++) {
                            System.out.println("- " + in.readUTF());
                        }
                        break;

                    case "UPLOAD":
                        System.out.print("Enter file path to upload: ");
                        String filePath = scanner.nextLine();
                        File file = new File(filePath);
                        if (!file.exists()) {
                            System.out.println("File not found.");
                            break;
                        }
                        out.writeUTF(file.getName());
                        out.writeLong(file.length());

                        try (FileInputStream fis = new FileInputStream(file)) {
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = fis.read(buffer)) > 0) {
                                out.write(buffer, 0, bytesRead);
                            }
                        }
                        System.out.println(in.readUTF());
                        break;

                    case "DOWNLOAD":
                        System.out.print("Enter filename to download: ");
                        String downloadName = scanner.nextLine();
                        out.writeUTF(downloadName);

                        String response = in.readUTF();
                        if (response.equals("FOUND")) {
                            long size = in.readLong();
                            FileOutputStream fos = new FileOutputStream("downloaded_" + downloadName);
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            long remaining = size;
                            while ((bytesRead = in.read(buffer, 0, (int)Math.min(buffer.length, remaining))) > 0) {
                                fos.write(buffer, 0, bytesRead);
                                remaining -= bytesRead;
                                if (remaining == 0) break;
                            }
                            fos.close();
                            System.out.println("File downloaded as: downloaded_" + downloadName);
                        } else {
                            System.out.println("File not found on server.");
                        }
                        break;

                    case "QUIT":
                        System.out.println("Disconnected.");
                        socket.close();
                        return;

                    default:
                        System.out.println("Unknown command.");
                        break;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}


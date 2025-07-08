import java.io.*;
import java.net.*;

public class IoTServer {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        System.out.println("IoT Server started on port " + PORT);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Received from device: " + line);
            }
        } catch (IOException e) {
            System.out.println("Device disconnected.");
        }
    }
}

public class IoTDeviceSimulator {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 5000;
    private static final String DEVICE_ID = "DEVICE-1001";

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Connected to server. Sending sensor data...");

            Random random = new Random();
            while (true) {
                int temp = 20 + random.nextInt(15);  
                String payload = String.format("{ \"deviceId\": \"%s\", \"temperature\": %d, \"timestamp\": %d }",
                        DEVICE_ID, temp, System.currentTimeMillis());

                out.println(payload);
                Thread.sleep(3000); 
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}


import java.io.*;
import java.net.*;

public class UDPFileSender {
    private static final int PORT = 9876;
    private static final int PACKET_SIZE = 1024;

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java UDPFileSender <receiver-ip> <file-path>");
            return;
        }

        String receiverIp = args[0];
        String filePath = args[1];

        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress receiverAddress = InetAddress.getByName(receiverIp);

            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[PACKET_SIZE];
            byte[] nameBytes = file.getName().getBytes();
            DatagramPacket namePacket = new DatagramPacket(nameBytes, nameBytes.length, receiverAddress, PORT);
            socket.send(namePacket);
            Thread.sleep(100); 

            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                DatagramPacket packet = new DatagramPacket(buffer, bytesRead, receiverAddress, PORT);
                socket.send(packet);
                Thread.sleep(5); 
            }
            byte[] end = "END".getBytes();
            DatagramPacket endPacket = new DatagramPacket(end, end.length, receiverAddress, PORT);
            socket.send(endPacket);

            fis.close();
            socket.close();
            System.out.println("File sent successfully.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

public class UDPFileReceiver {
    private static final int PORT = 9876;
    private static final int PACKET_SIZE = 1024;

    public static void main(String[] args) {
        try {
            DatagramSocket socket = new DatagramSocket(PORT);
            byte[] buffer = new byte[PACKET_SIZE];

            System.out.println("Waiting for file...");
            DatagramPacket namePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(namePacket);
            String fileName = new String(namePacket.getData(), 0, namePacket.getLength());
            FileOutputStream fos = new FileOutputStream("received_" + fileName);
            System.out.println("Receiving file: " + fileName);

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String msg = new String(packet.getData(), 0, packet.getLength());
                if (msg.equals("END")) {
                    System.out.println("File received successfully.");
                    break;
                }
                fos.write(packet.getData(), 0, packet.getLength());
            }

            fos.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


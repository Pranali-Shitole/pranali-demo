import java.io.*;
import java.net.*;

public class SMTPClient {
    public static void main(String[] args) {
        String smtpServer = "localhost";
        int port = 25;

        try (Socket socket = new Socket(smtpServer, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println(in.readLine()); 

            out.println("HELO localhost");
            System.out.println(in.readLine());

            out.println("MAIL FROM:<sender@example.com>");
            System.out.println(in.readLine());

            out.println("RCPT TO:<recipient@example.com>");
            System.out.println(in.readLine());

            out.println("DATA");
            System.out.println(in.readLine());
            out.println("Subject: Test Email from Java");
            out.println("From: sender@example.com");
            out.println("To: recipient@example.com");
            out.println(); 
            out.println("Hello! This is a test email sent from Java SMTP client.");
            out.println(".");
            System.out.println(in.readLine());

            out.println("QUIT");
            System.out.println(in.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class POP3Client {
    public static void main(String[] args) {
        String pop3Server = "localhost";
        int port = 110;

        try (Socket socket = new Socket(pop3Server, port);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println(in.readLine()); 

            out.println("USER username");
            System.out.println(in.readLine());

            out.println("PASS password");
            System.out.println(in.readLine());

            out.println("STAT"); 
            System.out.println(in.readLine());

            out.println("LIST"); 
            String line;
            while (!(line = in.readLine()).equals(".")) {
                System.out.println(line);
            }

            out.println("RETR 1"); 
            while (!(line = in.readLine()).equals(".")) {
                System.out.println(line);
            }

            out.println("QUIT");
            System.out.println(in.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


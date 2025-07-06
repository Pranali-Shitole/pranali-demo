import java.io.*;
import java.security.KeyStore;
import javax.net.ssl.*;

public class SecureShellServer {

    public static void main(String[] args) throws Exception {
        int port = 8443;
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream("server_keystore.jks"), "password".toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(keyStore, "password".toCharArray());

        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(kmf.getKeyManagers(), null, null);

        SSLServerSocketFactory ssf = ctx.getServerSocketFactory();
        SSLServerSocket serverSocket = (SSLServerSocket) ssf.createServerSocket(port);
        System.out.println("Secure Shell Server running on port " + port);

        while (true) {
            try (SSLSocket socket = (SSLSocket) serverSocket.accept()) {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                String command;
                while ((command = in.readLine()) != null) {
                    System.out.println("Executing: " + command);
                    Process process = Runtime.getRuntime().exec(command);
                    BufferedReader output = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = output.readLine()) != null) {
                        out.println(line);
                    }
                    out.println("END");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


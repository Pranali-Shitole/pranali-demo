import java.io.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;

public class TlsClientDemo {

    public static void main(String[] args) {
        String host = "www.google.com";
        int port = 443;

        try {
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
            socket.setEnabledCipherSuites(socket.getSupportedCipherSuites());

            System.out.println("Starting TLS handshake...");
            socket.startHandshake(); 

            System.out.println("TLS Handshake completed successfully.");
            SSLSession session = socket.getSession();
            Certificate[] certs = session.getPeerCertificates();
            System.out.println("Server Certificate:");
            for (Certificate cert : certs) {
                System.out.println(((X509Certificate) cert).getSubjectDN());
            }
            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())));
            out.println("GET / HTTP/1.1");
            out.println("Host: " + host);
            out.println("Connection: close");
            out.println(); 
            out.flush();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);
            }
            in.close();
            out.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


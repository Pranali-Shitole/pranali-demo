import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class CDNProxyServer {

    private static final int PORT = 8080;
    private static final String ORIGIN_SERVER = "http://example.com"; 
    private static final Map<String, byte[]> cache = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/", new ProxyHandler());
        server.setExecutor(Executors.newFixedThreadPool(10));
        System.out.println("CDN Proxy Server is running on port " + PORT);
        server.start();
    }

    static class ProxyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            URI requestURI = exchange.getRequestURI();
            String path = requestURI.toString();
            System.out.println("Received request: " + path);

            if (!method.equalsIgnoreCase("GET")) {
                sendResponse(exchange, 405, "Only GET is supported.".getBytes());
                return;
            }

            if (cache.containsKey(path)) {
                System.out.println("Serving from cache: " + path);
                sendResponse(exchange, 200, cache.get(path));
                return;
            }
            String targetURL = ORIGIN_SERVER + path;
            System.out.println("Fetching from origin: " + targetURL);

            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(targetURL).openConnection();
                conn.setRequestMethod("GET");

                int status = conn.getResponseCode();
                byte[] response = conn.getInputStream().readAllBytes();
                cache.put(path, response);
                sendResponse(exchange, status, response);

            } catch (IOException e) {
                e.printStackTrace();
                sendResponse(exchange, 502, "Bad Gateway".getBytes());
            }
        }

        private void sendResponse(HttpExchange exchange, int status, byte[] response) throws IOException {
            exchange.sendResponseHeaders(status, response.length);
            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        }
    }
}


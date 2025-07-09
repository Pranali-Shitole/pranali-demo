import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoStreamingServer {
    private static final int PORT = 8000;
    private static final String VIDEO_PATH = "sample.mp4"; 

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/video", new VideoHandler());
        ExecutorService executor = Executors.newFixedThreadPool(10);
        server.setExecutor(executor);
        System.out.println("Video streaming server started at http://localhost:" + PORT + "/video");
        server.start();
    }

    static class VideoHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            File video = new File(VIDEO_PATH);
            if (!video.exists()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }

            long fileLength = video.length();
            String rangeHeader = exchange.getRequestHeaders().getFirst("Range");

            long start = 0, end = fileLength - 1;

            if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
                String[] parts = rangeHeader.replace("bytes=", "").split("-");
                try {
                    start = Long.parseLong(parts[0]);
                    if (parts.length > 1) {
                        end = Long.parseLong(parts[1]);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            if (start > end || start >= fileLength) {
                exchange.sendResponseHeaders(416, -1); 
                return;
            }

            long contentLength = end - start + 1;

            Headers responseHeaders = exchange.getResponseHeaders();
            responseHeaders.set("Content-Type", "video/mp4");
            responseHeaders.set("Accept-Ranges", "bytes");
            responseHeaders.set("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
            responseHeaders.set("Content-Length", String.valueOf(contentLength));

            exchange.sendResponseHeaders(206, contentLength); 

            try (RandomAccessFile raf = new RandomAccessFile(video, "r");
                 OutputStream os = exchange.getResponseBody()) {

                raf.seek(start);
                byte[] buffer = new byte[8192];
                long bytesRemaining = contentLength;
                int bytesRead;

                while (bytesRemaining > 0 &&
                      (bytesRead = raf.read(buffer, 0, (int) Math.min(buffer.length, bytesRemaining))) != -1) {
                    os.write(buffer, 0, bytesRead);
                    bytesRemaining -= bytesRead;
                }
            }
        }
    }
}


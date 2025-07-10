// ServiceB.java
import com.sun.net.httpserver.HttpServer;
import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class ServiceB {
    public static void main(String[] args) throws IOException {
        Tracer tracer = GlobalOpenTelemetry.getTracer("service-b");

        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        server.createContext("/process", exchange -> {
            Span span = tracer.spanBuilder("service-b-process").startSpan();
            try {
                System.out.println("Service B processing...");
                String response = "Service B done!";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } finally {
                span.end();
            }
        });
        server.start();
        System.out.println("Service B running at http://localhost:8081");
    }
}

public class ServiceA {
    public static void main(String[] args) throws Exception {
        Tracer tracer = GlobalOpenTelemetry.getTracer("service-a");
        Span parentSpan = tracer.spanBuilder("service-a-request").startSpan();

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://localhost:8081/process")
                    .build();

            try (Response response = client.newCall(request).execute()) {
                System.out.println("Response from Service B: " + response.body().string());
            }
        } finally {
            parentSpan.end();
        }
    }
}


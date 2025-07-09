import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
public class ApiGatewayApplication {

    private final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @GetMapping("/user/**")
    public ResponseEntity<?> forwardToUserService(RequestEntity<?> request) {
        String targetUrl = "http://localhost:8081" + request.getUrl().getPath().replace("/user", "");
        return restTemplate.exchange(targetUrl, request.getMethod(), request, String.class);
    }

    @GetMapping("/order/**")
    public ResponseEntity<?> forwardToOrderService(RequestEntity<?> request) {
        String targetUrl = "http://localhost:8082" + request.getUrl().getPath().replace("/order", "");
        return restTemplate.exchange(targetUrl, request.getMethod(), request, String.class);
    }
}


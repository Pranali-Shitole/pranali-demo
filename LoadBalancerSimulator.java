import java.util.*;

class Server {
    private final String name;
    private int load = 0;

    public Server(String name) {
        this.name = name;
    }

    public void handleRequest(String request) {
        load++;
        System.out.println("[" + name + "] Handling request: " + request);
        try {
            Thread.sleep(100); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        load--;
    }

    public int getLoad() {
        return load;
    }

    public String getName() {
        return name;
    }
}

class LoadBalancer {
    private final List<Server> servers = new ArrayList<>();
    private int currentIndex = 0;

    public void addServer(Server server) {
        servers.add(server);
    }
    public Server getNextServer() {
        if (servers.isEmpty()) return null;
        Server server = servers.get(currentIndex);
        currentIndex = (currentIndex + 1) % servers.size();
        return server;
    }
    public Server getLeastLoadedServer() {
        return servers.stream().min(Comparator.comparingInt(Server::getLoad)).orElse(null);
    }
}

public class LoadBalancerSimulator {
    public static void main(String[] args) {
        LoadBalancer lb = new LoadBalancer();
        lb.addServer(new Server("Server-1"));
        lb.addServer(new Server("Server-2"));
        lb.addServer(new Server("Server-3"));
        for (int i = 1; i <= 15; i++) {
            String request = "Request-" + i;
            Server server = lb.getNextServer(); 

            if (server != null) {
                final Server selectedServer = server;
                new Thread(() -> selectedServer.handleRequest(request)).start();
            } else {
                System.out.println("No servers available!");
            }

            try {
                Thread.sleep(50); 
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

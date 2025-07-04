import java.util.*;
import java.util.concurrent.*;

public class Broker {
    private final Map<String, List<Subscriber>> topicSubscribers = new ConcurrentHashMap<>();

    public void subscribe(String topic, Subscriber subscriber) {
        topicSubscribers.computeIfAbsent(topic, k -> new CopyOnWriteArrayList<>()).add(subscriber);
    }

    public void unsubscribe(String topic, Subscriber subscriber) {
        List<Subscriber> subscribers = topicSubscribers.get(topic);
        if (subscribers != null) {
            subscribers.remove(subscriber);
        }
    }

    public void publish(String topic, String message) {
        List<Subscriber> subscribers = topicSubscribers.get(topic);
        if (subscribers != null) {
            for (Subscriber sub : subscribers) {
                sub.receive(topic, message);
            }
        }
    }
}
public interface Subscriber {
    void receive(String topic, String message);
}
public class SimpleSubscriber implements Subscriber {
    private final String name;

    public SimpleSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void receive(String topic, String message) {
        System.out.println("[" + name + "] received on topic '" + topic + "': " + message);
    }
}
public class Main {
    public static void main(String[] args) {
        Broker broker = new Broker();

        Subscriber alice = new SimpleSubscriber("Alice");
        Subscriber bob = new SimpleSubscriber("Bob");

        broker.subscribe("news", alice);
        broker.subscribe("news", bob);
        broker.subscribe("sports", alice);

        broker.publish("news", "Breaking news: Java 22 released!");
        broker.publish("sports", "Live: Local team wins!");

        broker.unsubscribe("news", bob);

        broker.publish("news", "Update: Java 22 patch released!");
    }
}

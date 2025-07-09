import org.eclipse.paho.client.mqttv3.*;

public class MqttExample {

    private static final String BROKER_URL = "tcp://broker.hivemq.com:1883";
    private static final String TOPIC = "test/topic/java";
    private static final String CLIENT_ID_PUB = "JavaPublisher";
    private static final String CLIENT_ID_SUB = "JavaSubscriber";

    public static void main(String[] args) throws Exception {
        Thread subscriberThread = new Thread(() -> startSubscriber());
        subscriberThread.start();
        Thread.sleep(2000);
        startPublisher("Hello from Java MQTT!");
    }

    private static void startSubscriber() {
        try {
            MqttClient client = new MqttClient(BROKER_URL, CLIENT_ID_SUB);
            client.connect();

            client.subscribe(TOPIC, (topic, msg) -> {
                String message = new String(msg.getPayload());
                System.out.println("[Subscriber] Received message: " + message);
            });

            System.out.println("[Subscriber] Subscribed to topic: " + TOPIC);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private static void startPublisher(String message) {
        try {
            MqttClient client = new MqttClient(BROKER_URL, CLIENT_ID_PUB);
            client.connect();

            MqttMessage mqttMessage = new MqttMessage(message.getBytes());
            mqttMessage.setQos(1);

            client.publish(TOPIC, mqttMessage);
            System.out.println("[Publisher] Message published: " + message);

            client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}


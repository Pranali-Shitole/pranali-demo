import redis.clients.jedis.Jedis;
import redis.clients.jedis.StreamEntryID;
import java.util.HashMap;
import java.util.Map;

public class LogProducer {
    private final Jedis jedis;
    private final String streamKey;

    public LogProducer(String redisHost, int redisPort, String streamKey) {
        this.jedis = new Jedis(redisHost, redisPort);
        this.streamKey = streamKey;
    }

    public void sendLog(String source, String level, String message) {
        Map<String, String> logData = new HashMap<>();
        logData.put("source", source);
        logData.put("level", level);
        logData.put("message", message);
        logData.put("timestamp", String.valueOf(System.currentTimeMillis()));

        jedis.xadd(streamKey, StreamEntryID.NEW_ENTRY, logData);
    }

    public void close() {
        jedis.close();
    }

    public static void main(String[] args) {
        LogProducer producer = new LogProducer("localhost", 6379, "logs_stream");

        for (int i = 0; i < 10; i++) {
            producer.sendLog("service-A", "INFO", "This is log message " + i);
        }

        producer.close();
    }
}


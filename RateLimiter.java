import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RateLimiter {
    private final JedisPool jedisPool;
    private final int maxRequests;
    private final int timeWindowInSeconds;

    public RateLimiter(String redisHost, int redisPort, int maxRequests, int timeWindowInSeconds) {
        this.jedisPool = new JedisPool(redisHost, redisPort);
        this.maxRequests = maxRequests;
        this.timeWindowInSeconds = timeWindowInSeconds;
    }

    public boolean isAllowed(String userId) {
        String key = "rate_limiter:" + userId;
        try (Jedis jedis = jedisPool.getResource()) {
            long currentCount = jedis.incr(key);

            if (currentCount == 1) {
                jedis.expire(key, timeWindowInSeconds);
            }

            return currentCount <= maxRequests;
        }
    }

    public void shutdown() {
        jedisPool.close();
    }
    public static void main(String[] args) throws InterruptedException {
        RateLimiter rateLimiter = new RateLimiter("localhost", 6379, 5, 60); // 5 requests per 60 seconds

        String userId = "user123";

        for (int i = 1; i <= 7; i++) {
            boolean allowed = rateLimiter.isAllowed(userId);
            System.out.println("Request " + i + " allowed: " + allowed);
            Thread.sleep(1000); 
        }

        rateLimiter.shutdown();
    }
}


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConcurrentWebCrawler {

    private static final int MAX_THREADS = 10;
    private static final String DOMAIN = "example.com";  

    private final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
    private final Set<String> visited = ConcurrentHashMap.newKeySet();
    private final Queue<String> toVisit = new ConcurrentLinkedQueue<>();

    public void startCrawling(String startUrl) {
        toVisit.add(startUrl);

        while (!toVisit.isEmpty() || !((ThreadPoolExecutor) executor).getQueue().isEmpty()) {
            String url = toVisit.poll();
            if (url != null && visited.add(url)) {
                executor.submit(() -> crawl(url));
            }
        }

        executor.shutdown();
    }

    private void crawl(String url) {
        System.out.println("Crawling: " + url);

        try {
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);

            if (conn.getResponseCode() != 200 || !conn.getContentType().contains("text/html")) {
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder page = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                page.append(line);
            }

            extractLinks(page.toString(), url);

        } catch (Exception e) {
            System.err.println("Failed to crawl: " + url + " — " + e.getMessage());
        }
    }

    private void extractLinks(String html, String baseUrl) {
        Pattern linkPattern = Pattern.compile("href=[\"'](http[s]?://[^\"'#\\s]+)[\"']", Pattern.CASE_INSENSITIVE);
        Matcher matcher = linkPattern.matcher(html);

        while (matcher.find()) {
            String foundUrl = matcher.group(1);
            if (isSameDomain(foundUrl)) {
                toVisit.add(foundUrl);
            }
        }
    }

    private boolean isSameDomain(String url) {
        try {
            URI uri = new URI(url);
            return uri.getHost() != null && uri.getHost().endsWith(DOMAIN);
        } catch (URISyntaxException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String startUrl = "https://example.com";
        new ConcurrentWebCrawler().startCrawling(startUrl);
    }
}


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResumableDownloader {

    public static void downloadWithResume(String fileURL, String outputPath) throws IOException {
        File outputFile = new File(outputPath);
        long existingFileSize = outputFile.exists() ? outputFile.length() : 0;

        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        if (existingFileSize > 0) {
            httpConn.setRequestProperty("Range", "bytes=" + existingFileSize + "-");
            System.out.println("Resuming from byte position: " + existingFileSize);
        }

        int responseCode = httpConn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_PARTIAL) {
            long totalFileSize = existingFileSize + httpConn.getContentLengthLong();
            try (InputStream inputStream = httpConn.getInputStream();
                 RandomAccessFile raf = new RandomAccessFile(outputFile, "rw")) {

                raf.seek(existingFileSize); 

                byte[] buffer = new byte[4096];
                int bytesRead;
                long downloaded = existingFileSize;

                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    raf.write(buffer, 0, bytesRead);
                    downloaded += bytesRead;
                    printProgress(downloaded, totalFileSize);
                }
            }
            System.out.println("\nDownload completed.");
        } else {
            System.out.println("Server responded with code: " + responseCode);
        }
        httpConn.disconnect();
    }

    private static void printProgress(long downloaded, long total) {
        int progress = (int) ((downloaded * 100) / total);
        System.out.print("\rProgress: " + progress + "%");
    }

    public static void main(String[] args) {
        String fileURL = "https://example.com/largefile.zip";
        String outputPath = "largefile.zip";

        try {
            downloadWithResume(fileURL, outputPath);
        } catch (IOException e) {
            System.out.println("Download failed: " + e.getMessage());
        }
    }
}

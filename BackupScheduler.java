import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

public class BackupScheduler {

    private static final String SOURCE_DIR = "C:/mydata";
    private static final String BACKUP_DIR = "D:/backups";

    private static final int INITIAL_DELAY = 0; 
    private static final int PERIOD = 1; 

    public static void main(String[] args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable backupTask = () -> {
            try {
                String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
                Path sourcePath = Paths.get(SOURCE_DIR);
                Path targetPath = Paths.get(BACKUP_DIR, "backup-" + timestamp);

                System.out.println("Starting backup: " + targetPath);
                copyDirectory(sourcePath, targetPath);
                System.out.println("Backup complete.");
            } catch (IOException e) {
                System.err.println("Backup failed: " + e.getMessage());
                e.printStackTrace();
            }
        };
        scheduler.scheduleAtFixedRate(backupTask, INITIAL_DELAY, PERIOD, TimeUnit.HOURS);
    }

    private static void copyDirectory(Path source, Path target) throws IOException {
        if (!Files.exists(source)) {
            throw new IllegalArgumentException("Source directory does not exist: " + source);
        }

        Files.walkFileTree(source, new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                Path targetDir = target.resolve(source.relativize(dir));
                Files.createDirectories(targetDir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                Path targetFile = target.resolve(source.relativize(file));
                Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}


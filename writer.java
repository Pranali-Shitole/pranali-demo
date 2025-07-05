import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class Writer {
    public static void main(String[] args) throws Exception {
        try (RandomAccessFile file = new RandomAccessFile("shared_mem.dat", "rw");
             FileChannel channel = file.getChannel()) {

            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 1024);

            String message = "Hello from Writer!";
            byte[] data = message.getBytes();

            buffer.putInt(data.length);
            buffer.put(data);

            System.out.println("Writer wrote: " + message);
        }
    }
}

public class Reader {
    public static void main(String[] args) throws Exception {
        try (RandomAccessFile file = new RandomAccessFile("shared_mem.dat", "r");
             FileChannel channel = file.getChannel()) {

            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, 1024);

            int length = buffer.getInt();
            byte[] data = new byte[length];
            buffer.get(data);

            String message = new String(data);
            System.out.println("Reader read: " + message);
        }
    }
}


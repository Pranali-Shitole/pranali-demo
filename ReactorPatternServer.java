import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class ReactorPatternServer {
    public static void main(String[] args) throws IOException {
        Reactor reactor = new Reactor(9090);
        reactor.run(); 
    }
}

class Reactor implements Runnable {
    private final Selector selector;
    private final ServerSocketChannel serverSocket;

    public Reactor(int port) throws IOException {
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.configureBlocking(false);
        serverSocket.bind(new InetSocketAddress(port));
        SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        sk.attach(new Acceptor());
    }

    public void run() {
        System.out.println("Reactor started on port " + ((InetSocketAddress) serverSocket.getLocalAddress()).getPort());

        try {
            while (!Thread.interrupted()) {
                selector.select(); 
                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectedKeys.iterator();

                while (it.hasNext()) {
                    dispatch(it.next());
                    it.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dispatch(SelectionKey key) {
        Runnable handler = (Runnable) key.attachment();
        if (handler != null) {
            handler.run();
        }
    }
    class Acceptor implements Runnable {
        public void run() {
            try {
                SocketChannel client = serverSocket.accept();
                if (client != null) {
                    new Handler(selector, client);
                    System.out.println("Accepted new connection from " + client.getRemoteAddress());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
class Handler implements Runnable {
    private final SocketChannel socket;
    private final SelectionKey sk;
    private final ByteBuffer buffer = ByteBuffer.allocate(1024);

    public Handler(Selector selector, SocketChannel client) throws IOException {
        socket = client;
        socket.configureBlocking(false);
        sk = socket.register(selector, SelectionKey.OP_READ);
        sk.attach(this); 
        selector.wakeup();
    }

    public void run() {
        try {
            buffer.clear();
            int read = socket.read(buffer);
            if (read == -1) {
                socket.close();
                System.out.println("Client disconnected");
                return;
            }

            buffer.flip();
            String received = new String(buffer.array(), 0, buffer.limit());
            System.out.println("Received: " + received.trim());
            socket.write(ByteBuffer.wrap(("Echo: " + received).getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}


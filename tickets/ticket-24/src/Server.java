import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Server {
    private static final int PORT = 8080;
    private DatagramSocket socket;
    private ConcurrentLinkedQueue<User> users;

    public static void main(String[] args) {
        new Server().start();
    }

    public void start() {
        try {
            socket = new DatagramSocket(PORT);
        } catch (SocketException e) {
            System.err.println("Can't create socket with port " + PORT + ". Server didn't start.");
            close();
            return;
        }
        users = new ConcurrentLinkedQueue<>();
        run();
    }

    public void close() {
        socket.close();
    }

    private void run() {
        DatagramPacket packet;
        String message, name, id;
        InetAddress address;
        int clientPort;
        boolean found;
        try {
            while (!Thread.interrupted()) {
                packet = new DatagramPacket(
                        new byte[socket.getReceiveBufferSize()],
                        socket.getReceiveBufferSize());
                socket.receive(packet);
                message = new String(
                        packet.getData(), packet.getOffset(),
                        packet.getLength(), StandardCharsets.UTF_8);
                name = message;
                address = packet.getAddress();
                clientPort = packet.getPort();
                id = address.toString() + ":" + clientPort;
                found = false;
                for (var user : users) {
                    if (user.getId().equals(id)) {
                        found = true;
                        name = user.getName();
                        break;
                    }
                }
                if (!found) {
                    users.add(new User(address, clientPort, id, name));
                }
                if (name.equals(message)) {
                    message = name + "(" + id + ") registered successfully!";
                } else {
                    message = name + "(" + id + ") > " + message;
                }
                System.out.println(message);
                for (User user : users) {
                    packet = new DatagramPacket(message.getBytes(), message.getBytes().length, user.getAddress(), user.getPort());
                    socket.send(packet);
                }
            }
            close();
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            close();
        }
    }

    private static class User {
        private final InetAddress address;
        private final int port;
        private final String id;
        private final String name;

        public User(InetAddress address, int port, String id, String name) {
            this.address = address;
            this.port = port;
            this.id = id;
            this.name = name;
        }

        public InetAddress getAddress() {
            return address;
        }

        public int getPort() {
            return port;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}

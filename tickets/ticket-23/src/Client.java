import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class Client {

    private static final Scanner scanner = new Scanner(System.in);
    private static final int PORT = 8080;
    private static final String HOST = "localhost";

    public static void main(String[] args) {
        new Client().run(scanner.nextLine());
    }

    private void run(String name) {
        InetAddress address;
        try {
            address = InetAddress.getByName(HOST);
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + HOST);
            return;
        }
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        DatagramSocket finalSocket = socket;
        Thread receiver = new Thread(() -> receiveMessage(finalSocket));
        DatagramSocket finalSocket1 = socket;
        Thread sender = new Thread(() -> sendMessage(finalSocket1, address, name));
        receiver.start();
        sender.start();
    }

    private void receiveMessage(DatagramSocket socket) {
        byte[] buffer;
        try {
            buffer = new byte[socket.getReceiveBufferSize()];
        } catch (SocketException e) {
            System.err.println(e.getLocalizedMessage());
            return;
        }
        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println(received);
            } catch (IOException e) {
                System.err.println(e.getLocalizedMessage());
                return;
            }
        }
    }

    private void sendMessage(DatagramSocket socket, InetAddress address, String name) {
        DatagramPacket packet;
        try {
            packet = new DatagramPacket(name.getBytes(), name.getBytes().length, address, PORT);
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String message;
        while (true) {
            try {
                message = scanner.nextLine();
                packet = new DatagramPacket(message.getBytes(), message.getBytes().length, address, PORT);
                socket.send(packet);
            } catch (IOException e) {
                System.err.println(e.getLocalizedMessage());
                return;
            }
        }
    }
}
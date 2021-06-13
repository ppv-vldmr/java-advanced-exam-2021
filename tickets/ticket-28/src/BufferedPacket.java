package src;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

public class BufferedPacket {
    private static final String FAILURE_SEND = "Failure while sending message";
    private static final String FAILURE_RECEIVE= "Failure while receiving message";

    private final DatagramPacket packet;
    private final byte[] buffer;

    BufferedPacket(final int bufferSize) {
        buffer = new byte[bufferSize];
        packet = new DatagramPacket(buffer, bufferSize);
    }

    BufferedPacket(final int bufferSize, final SocketAddress socketAddress) {
        buffer = new byte[bufferSize];
        packet = new DatagramPacket(buffer, buffer.length, socketAddress);
    }

    void send(final DatagramSocket socket, final String data) throws IOException {
        packet.setData(data.getBytes(StandardCharsets.UTF_8));
        socket.send(packet);
    }

    void sendSafe(final DatagramSocket socket, final String data) {
        try {
            this.send(socket, data);
        } catch (final IOException e) {
            System.err.println(FAILURE_SEND + ": " + e.getMessage());
        }
    }

    private void receive(final DatagramSocket socket) throws IOException {
        packet.setData(buffer);
        packet.setLength(buffer.length);
        socket.receive(packet);
    }

    void receiveSafe(final DatagramSocket socket) {
        try {
            this.receive(socket);
        } catch (final IOException e) {
            System.err.println(FAILURE_RECEIVE + ": " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return new String(packet.getData(), packet.getOffset(), packet.getLength(), StandardCharsets.UTF_8);
    }
}

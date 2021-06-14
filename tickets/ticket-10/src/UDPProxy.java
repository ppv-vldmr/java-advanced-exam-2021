
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;
import java.util.concurrent.*;

public class UDPProxy {
    private Map<Integer, List<SocketAddress> > table;
    private Map<Integer, Queue<Response> > responses;
    private ExecutorService executors;
    private volatile boolean Closed;
    private Selector selector;

    public void start(final int threads, final Line... lines) {
        if (threads <= 0) {
            throw new IllegalArgumentException("Number of thread must be at least 1!");
        }
        try {
            selector = Selector.open();
            Closed = false;
            executors = Executors.newFixedThreadPool(threads + 1);
            table = new ConcurrentHashMap<>();
            responses = new ConcurrentHashMap<>();
            for (final Line line : lines) {
                int localPort = line.localPort;
                final SocketAddress address = line.createAddress();
                if (!table.containsKey(localPort)) {
                    table.put(localPort, new ArrayList<>());
                    responses.put(localPort, new ArrayBlockingQueue<>(10000));
                    final DatagramChannel channel = DatagramChannel.open();
                    channel.configureBlocking(false);
                    channel.bind(new InetSocketAddress(localPort));
                    channel.register(selector, SelectionKey.OP_READ);
                }
                table.get(localPort).add(address);
            }
            executors.submit(this::run);
        } catch (final IOException e) {
            processException("Can't start", e);
        }
    }

    public void close() {
        try {
            Closed = true;
            shutdownAndAwaitTermination(executors);
            for (SelectionKey key : selector.keys()) {
                key.channel().close();
            }
            selector.close();
        } catch (final IOException e) {
            processException("Can't close", e);
        }
    }

    private void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow();
                if (!pool.awaitTermination(10, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void run() {
        while (!Thread.interrupted() && !Closed) {
            try {
                selector.select(100);
            } catch (final IOException e) {
                processException("Can't wait", e);
            }
            if (!selector.selectedKeys().isEmpty()) {
                for (final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator(); iterator.hasNext();) {
                    try {
                        final SelectionKey key = iterator.next();
                        DatagramChannel channel = (DatagramChannel) key.channel();
                        if (key.isReadable()) {
                            try {
                                final ByteBuffer buffer = ByteBuffer.allocate(channel.socket().getReceiveBufferSize());
                                SocketAddress address = channel.receive(buffer);
                                final int localPort = channel.socket().getLocalPort();
                                executors.submit(() -> {
                                    dialog(buffer, localPort, address);
                                    key.interestOps(SelectionKey.OP_WRITE);
                                    selector.wakeup();
                                });
                            } catch (IOException e) {
                                processException("Can't receive task from local port", e);
                            }
                        }
                        if (key.isWritable()) {
                            int localPort = channel.socket().getLocalPort();
                            Queue<Response> order = responses.get(localPort);
                            while (order.size() > 0) {
                                try {
                                    Response response = order.poll();
                                    channel.send(response.getBuffer(), response.getAddress());
                                } catch (final IOException e) {
                                    processException("Can't send!", e);
                                }
                            }
                            key.interestOps(SelectionKey.OP_READ);
                        }
                    } finally {
                        iterator.remove();
                    }
                }
            }
        }
    }

    private void dialog(final ByteBuffer buffer, final int localPort, final SocketAddress send) {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(100);
            int size = socket.getReceiveBufferSize();
            final DatagramPacket response = new DatagramPacket(new byte[size], size);
            for (final SocketAddress address : table.get(localPort)) {
                final DatagramPacket message = new DatagramPacket(buffer.flip().array(), buffer.capacity(), address);
                boolean received = false;
                while (!Thread.interrupted() && !socket.isClosed() && !received) {
                    try {
                        socket.send(message);
                    } catch (final IOException e) {
                        processException("Message hasn't send", e);
                        continue;
                    }
                    try {
                        socket.receive(response);
                        responses.get(localPort).add(new Response(send, ByteBuffer.wrap(response.getData())));
                        received = true;
                    } catch (final IOException e) {
                        processException("Can't receive!", e);
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void processException(final String message, final Exception e) {
        System.out.println(message + " " + e.getMessage());
    }

    private static class Response {
        private final ByteBuffer buffer;
        private final SocketAddress address;

        Response(final SocketAddress address, final ByteBuffer buffer) {
            this.address = address;
            this.buffer = ByteBuffer.allocate(buffer.capacity());
            buffer.clear().put(buffer);
        }

        ByteBuffer getBuffer() {
            return buffer.flip();
        }

        SocketAddress getAddress() {
            return address;
        }
    }
}

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class Line {
    final int localPort;
    private final InetAddress ip;
    private final int remotePort;

    public Line(final int localPort, final InetAddress ip, final int remotePort) {
        this.localPort = localPort;
        this.ip = ip;
        this.remotePort = remotePort;
    }

    public SocketAddress createAddress() {
        return new InetSocketAddress(ip, remotePort);
    }
}

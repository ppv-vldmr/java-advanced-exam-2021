import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class StringManager extends UnicastRemoteObject implements Manager {
    private final static int DEFAULT_PORT = 8888;
    private final List<String> data = new ArrayList<>();

    public static void main(String[] args) {
        if (args == null || Arrays.stream(args).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Args is null or contains null");
        }

        final int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        startNewServer(port);
    }

    public static void startNewServer(final int port) {
        try {
            final Manager server = new StringManager(port);
            Naming.rebind("//localhost/server", server);
        } catch (final RemoteException e) {
            System.err.println("Cannot export object: " + e.getMessage());
        } catch (final MalformedURLException ignored) {}
        System.out.println("Server started");
    }

    public StringManager() throws RemoteException {
    }

    public StringManager(int port) throws RemoteException {
        super(port);
    }

    @Override
    public synchronized void addString(String s) throws RemoteException {
        data.add(s);
        System.out.println("Added: " + s);
    }

    @Override
    public synchronized boolean deleteLastString() throws RemoteException {
        if (data.isEmpty()) {
            System.out.println("Nothing deleted, list is empty");
            return false;
        }
        System.out.println("Deleted: " + data.get(data.size() - 1));
        data.remove(data.size() - 1);
        return true;
    }

    @Override
    public boolean deleteString(String s) throws RemoteException {
        boolean ret = data.remove(s);
        System.out.println(ret ? "Deleted: " + s : "Nothing deleted");
        return ret;
    }

    @Override
    public synchronized List<String> getAllStrings() throws RemoteException {
        System.out.println("Client get all strings");
        return data;
    }
}

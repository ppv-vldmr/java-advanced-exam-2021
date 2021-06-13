import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Client {
    public static Manager serverLookup() throws RemoteException {
        try {
            return (Manager) Naming.lookup("//localhost/server");
        } catch (final NotBoundException e) {
            System.err.println("Server is not bound");
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        if (args == null || Arrays.stream(args).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Args is null or contains null");
        }
        if (args.length == 0) {
            throw new IllegalArgumentException("No args");
        }

        try {
            Manager server = serverLookup();
            if (server == null) {
                return;
            }
            processArgs(server, args);
        } catch (final RemoteException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void processArgs(Manager server, String[] args) throws RemoteException {
        switch (args[0]) {
            case "add": {
                if (args.length != 2) {
                    throw new IllegalArgumentException("Expected second argument");
                }
                server.addString(args[1]);
                break;
            }
            case "delete": {
                if (args.length == 1 ? server.deleteLastString() : server.deleteString(args[1])) {
                    System.out.println("String deleted");
                } else {
                    System.out.println("Cannot delete");
                }
                break;
            }
            case "list": {
                if (args.length > 1) {
                    throw new IllegalArgumentException("Expected 1 argument");
                }
                System.out.println(
                        server
                                .getAllStrings()
                                .stream()
                                .collect(Collectors.joining(System.lineSeparator()))
                );
                break;
            }
            default:
                throw new UnsupportedOperationException();
        }
    }
}

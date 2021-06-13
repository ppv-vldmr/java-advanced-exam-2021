import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Client {
    public static void main(String[] args) {
        if (args == null || Arrays.stream(args).anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Args is null or contains null");
        }
        if (args.length == 0) {
            throw new IllegalArgumentException("No args");
        }

        try {
            Server server;
            try {
                server = (Server) Naming.lookup("//localhost/server");
            } catch (final NotBoundException e) {
                System.out.println("Server is not bound");
                return;
            } catch (final MalformedURLException e) {
                System.err.println("Malformed URL");
                return;
            }

            switch (args[0]) {
                case "add": {
                    if (args.length != 2) {
                        throw new IllegalArgumentException("Expected second argument");
                    }
                    server.addString(args[1]);
                    break;
                }
                case "delete": {
                    if (args.length == 1) {
                        server.deleteLastString();
                    } else {
                        server.deleteString(args[1]);
                    }
                    break;
                }
                case "list": {
                    System.out.print(
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
        } catch (final RemoteException e) {
            System.err.println(e.getMessage());
        }
    }
}

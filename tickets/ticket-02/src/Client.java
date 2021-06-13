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
            Manager server;
            try {
                server = (Manager) Naming.lookup("//localhost/server");
            } catch (final NotBoundException e) {
                System.out.println("Server is not bound");
                return;
            } catch (MalformedURLException e) {
                e.printStackTrace();
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

import core.Core;
import core.RemoteCore;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server {
    final static String NAME = "//localhost/core";
    final static int DEFAULT_PORT = 8888;

    public static void main(final String[] args) {
        int port = args.length > 1 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
        Registry registry;
        try {
            registry = LocateRegistry.createRegistry(port);
        } catch (RemoteException e) {
            System.err.println("Can not create registry");
            return;
        }
        final Core core = new RemoteCore();
        try {
            UnicastRemoteObject.exportObject(core, port);
            registry.rebind(NAME, core);
            System.out.println("Server started");
        } catch (final RemoteException e) {
            System.out.println("Cannot export object: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}

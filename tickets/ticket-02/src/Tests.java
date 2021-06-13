import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

@RunWith(JUnit4.class)
public class Tests {
    private static Manager server;
    private static final String URL = "//localhost/server";

    @BeforeClass
    public static void init() throws RemoteException, NotBoundException {
        try {
            LocateRegistry.createRegistry(1099);
        } catch (RemoteException ignored) {}
        try {
            final Manager exportServer = new StringManager();
            Naming.rebind(URL, exportServer);
        } catch (final RemoteException | MalformedURLException e) {
            System.out.println("Cannot export object: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            server = (Manager) Naming.lookup(URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        Client.main(new String[]{"delete"});
        Client.main(new String[]{"add", "test1"});
        Client.main(new String[]{"add", "test2"});
        Client.main(new String[]{"add", "test3"});
        Client.main(new String[]{"list"});
        Client.main(new String[]{"delete"});
        Client.main(new String[]{"delete", "test1"});
        Client.main(new String[]{"delete"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void nullArgs() {
        Client.main(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void someArgsIsNull() {
        Client.main(new String[]{"add", null});
    }

    @Test(expected = IllegalArgumentException.class)
    public void addWithoutArgs() {
        Client.main(new String[]{"add"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooManyArgs() {
        Client.main(new String[]{"list", "a", "b"});
    }

    @Test(expected = UnsupportedOperationException.class)
    public void unsupportedOperation() {
        Client.main(new String[]{"test", "test"});
    }
}
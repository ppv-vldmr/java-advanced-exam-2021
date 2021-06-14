import core.Core;
import core.RemoteCore;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Tests {
    private static final int PORT = 1099;
    private static int CNT = 0;

    private Core core;

    @BeforeEach
    void init() throws RemoteException, NotBoundException, MalformedURLException {
        if (CNT == 0) {
            LocateRegistry.createRegistry(PORT);
        }
        final Core remoteCore = new RemoteCore();
        UnicastRemoteObject.exportObject(remoteCore, PORT);
        Naming.rebind(Server.NAME, remoteCore);
        core = (Core) Naming.lookup(Server.NAME);

        ++CNT;

        addToCore(List.of("1", "3", "1", "2", "4"));
    }

    void addToCore(List<String> rows) throws RemoteException {
        for (String row : rows) {
            core.add(row);
        }
    }

    void assertEquals(String... expected) throws RemoteException {
        List<String> expectedRows = IntStream.range(0, expected.length).mapToObj(i -> i + " " + expected[i]).collect(Collectors.toList());
        Assertions.assertEquals(expectedRows, core.list());
    }

    @Test
    void testAdd() throws RemoteException {
        assertEquals("1", "3", "1", "2", "4");
    }

    @Test
    void testRemove() throws RemoteException {
        core.remove("1");
        assertEquals("3", "2", "4");
        core.remove("2");
        assertEquals("3", "4");
    }

    @Test
    void testRemoveByIndex() throws RemoteException {
        core.removeByIndex(0);
        assertEquals("3", "1", "2", "4");
        core.removeByIndex(1);
        assertEquals("3", "2", "4");
    }

    @Test
    void testContains() throws RemoteException {
        Assertions.assertEquals(List.of(true, false), core.contains(List.of("2", "5")));
    }

    @Test
    void testQueries() throws RemoteException {
        core.remove("5");
        core.remove("1");
        Assertions.assertEquals(core.queries(), List.of("add 1", "add 3", "add 1", "add 2", "add 4", "remove 5", "remove 1", "queries"));
    }
}

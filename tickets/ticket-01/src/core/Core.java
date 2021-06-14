package core;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface Core extends Remote {
    void add(String row) throws RemoteException;

    int remove(String row) throws RemoteException;

    void removeByIndex(int index) throws RemoteException;

    List<String> list() throws RemoteException;

    List<Boolean> contains(List<String> rows) throws RemoteException;

    List<String> queries() throws RemoteException;
}

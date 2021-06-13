import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Manager extends Remote {
    void addString(String s) throws RemoteException;

    boolean deleteLastString() throws RemoteException;

    boolean deleteString(String s) throws RemoteException;

    List<String> getAllStrings() throws RemoteException;
}

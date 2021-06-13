import java.rmi.RemoteException;
import java.util.Scanner;

public class GUI {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Manager server = Client.serverLookup();
            if (server == null) {
                return;
            }
            String s;
            while (!(s = scanner.nextLine()).equals("q")) {
                try {
                    Client.processArgs(server, s.split(" "));
                } catch (IllegalArgumentException e) {
                    System.out.println("Illegal arguments");
                } catch (UnsupportedOperationException e) {
                    System.out.println("Unknown operation");
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

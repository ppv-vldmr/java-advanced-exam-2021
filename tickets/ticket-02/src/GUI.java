import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.Scanner;

public class GUI {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            Manager server = Client.serverLookup();
            if (server == null) {
                return;
            }
            String s;
            while (!(s = reader.readLine()).equals("q")) {
                try {
                    Client.processArgs(server, s.split(" "));
                } catch (IllegalArgumentException e) {
                    System.out.println("Illegal arguments");
                } catch (UnsupportedOperationException e) {
                    System.out.println("Unknown operation");
                }
            }
        } catch (IOException e) {
            System.err.println("Critical exception");
        }
    }
}

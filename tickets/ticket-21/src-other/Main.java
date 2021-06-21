
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class Main {
    private static final int SIZE = 100_000;

    public static void main(String[] args) {
        if (args == null || Arrays.stream(args).anyMatch(Objects::isNull)) {
            System.err.println("All args must not be null");
            return;
        }
        if (args.length != 2) {
            System.err.println("Wrong count of args");
            return;
        }
        try {
            int count = Integer.parseInt(args[0]),
                    tries = Integer.parseInt(args[1]);
            new Judge(tries, count, SIZE).begin();
        } catch (NumberFormatException e) {
            System.err.format("Wrong format of number : \n%s\n", e.getMessage());
        }
    }
}

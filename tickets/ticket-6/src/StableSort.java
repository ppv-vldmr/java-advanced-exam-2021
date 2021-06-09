import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class StableSort {
    public static void run(BufferedReader in, PrintStream out, PrintStream err) throws IOException {
        String line;
        HashMap<Integer, ArrayList<String>> map = new HashMap<>();
        while ((line = in.readLine()) != null) {
            String[] lexemes = line.split(" ");
            try {
                switch (lexemes[0]) {
                    case "add" -> {
                        Integer ind = Integer.parseInt(lexemes[1]);
                        map.putIfAbsent(ind, new ArrayList<>());
                        map.get(ind).add(lexemes[2]);
                    }
                    case "remove" -> map.remove(Integer.parseInt(lexemes[1]));
                    case "print" -> {
                        out.println("-----START-----");
                        map.forEach((ind, value) -> value.forEach(it -> out.println(ind + " " + it)));
                        out.println("------END------");
                    }
                    default -> err.print("Unexpected operation: " + lexemes[0]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                err.println("was less arguments then expected");
            } catch (NumberFormatException e) {
                err.println("error parsing number " + lexemes[1]);
            }
        }
    }

    public static void main(String[] args) {
        switch (args.length) {
            case 0 -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                    run(reader, System.out, System.err);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case 2 -> {
                try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
                    try (PrintStream writer = new PrintStream(new FileOutputStream(args[1]))) {
                        run(reader, writer, writer);
                    } catch (IOException e) {
                        System.err.println("Problems with output file : ");
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    System.err.println("Problems with input file : ");
                    e.printStackTrace();
                }
            }
            default -> System.err.println("Must be 2 or 0");
        }
    }

}

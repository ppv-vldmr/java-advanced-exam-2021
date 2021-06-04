import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static BufferedWriter out;

    static void print_err(String str) {
        System.out.println(str);
    }

    static void print(String str) throws IOException {
        if (out != null) {
            out.write(str + "\n");
        } else {
            System.out.println(str);
        }
    }

    public static void run(BufferedReader in) throws IOException {
        String line;
        HashMap<Long, ArrayList<String>> map = new HashMap<>();
        while ((line = in.readLine()) != null) {
            String[] lexemes = line.split(" ");
            try {
                switch (lexemes[0]) {
                    case "add" -> {
                        Long ind = Long.parseLong(lexemes[1]);
                        map.putIfAbsent(ind, new ArrayList<>());
                        map.get(ind).add(lexemes[2]);
                    }
                    case "remove" -> map.remove(Long.parseLong(lexemes[1]));
                    case "print" -> map.forEach((ind, value) -> value.forEach(
                            it -> {
                                try {
                                    print(ind + " " + it);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    ));
                    default -> throw new IllegalStateException("Unexpected value: " + lexemes[0]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                print_err("was less arguments then expected");
            } catch (NumberFormatException e) {
                print_err("error parsing number " + lexemes[1]);
            }
        }
    }


    public static void main(String[] args) {
        switch (args.length) {
            case 0 -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                    run(reader);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case 2 -> {
                try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]))) {
                        out = writer;
                        run(reader);
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

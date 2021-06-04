import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    static BufferedWriter out;
    static void print(String str) throws IOException {
        if(out!=null) {
            out.write(str+"\n");
        } else {
            System.out.print(str);
        }
    }

    public static void run(BufferedReader in) throws IOException {
        String line;
        HashMap<Long, ArrayList<String>> map = new HashMap<>();
        while ((line = in.readLine()) != null) {
            String[] lexemes = line.split(" ");
            switch (lexemes[0]) {
                case "add" -> {
                    Long ind = Long.parseLong(lexemes[1]);
                    map.putIfAbsent(ind,new ArrayList<>());
                    map.get(ind).add(lexemes[2]);
                }
                case "remove" -> map.remove(Long.parseLong(lexemes[1]));
                case "print" -> {
                    map.forEach((ind, value) -> value.forEach(
                            it -> {
                                try {
                                    print(ind + " " + it+"\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    ));
                }
                default -> throw new IllegalStateException("Unexpected value: " + lexemes[0]);
            }
        }
    }

    public static void main(String[] args) {
        switch (args.length) {
            case 0 -> {
                try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
                        run(reader);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            case 2 -> {
                try(BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
                    try(BufferedWriter writer = new BufferedWriter(new FileWriter(args[1]))) {
                        out = writer;
                        run(reader);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            default -> System.err.println("Must be 2 or 0");
        }
    }

}

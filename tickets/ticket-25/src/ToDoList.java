import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ToDoList {
    HashMap<String, String> incompleteTasks, completeTasks;

    static Map<String, MODE> to = new HashMap<>();

    enum MODE {INCOMPLETE, COMPLETE, ALL}

    static {
        to.put("incomplete", MODE.INCOMPLETE);
        to.put("complete", MODE.COMPLETE);
        to.put("all", MODE.ALL);
    }

    void print(MODE arg, PrintStream out, boolean mark) {
        switch (arg) {
            case INCOMPLETE -> incompleteTasks.forEach((label, task) -> out.format("* %s : %s\n", label, task));
            case COMPLETE -> completeTasks.forEach((label, task) -> out.format(mark ? "* ~~%s : %s~~\n" : "* %s : %s\n", label, task));
            case ALL -> {
                print(MODE.COMPLETE, out, true);
                print(MODE.INCOMPLETE, out, true);
            }
            default -> System.err.format("Unsupported label - %s\n", arg);
        }
    }

    void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            String[] lexemes;
            while ((line = in.readLine()) != null) {
                try{
                    lexemes = line.split(" ");
                    switch (lexemes[0]) {
                        case "add" -> {
                            if (lexemes.length < 3) {
                                try (BufferedReader fileIn = new BufferedReader(new FileReader(lexemes[1]))) {
                                    String record;
                                    while ((record = fileIn.readLine()) != null) {
                                        String[] words = record.split("/* ~~| : |~~|/* ");
                                        if (record.endsWith("~~")) completeTasks.put(words[0], words[1]);
                                        else incompleteTasks.put(words[0], words[1]);
                                    }
                                }
                            } else {
                                incompleteTasks.put(lexemes[1], Arrays.stream(lexemes).skip(2).collect(Collectors.joining(" ")));
                            }

                        }
                        case "mark" -> {
                            if (lexemes.length == 1) System.err.format("Must be two words");
                            String task;
                            if ((task = incompleteTasks.remove(lexemes[1])) != null) {
                                completeTasks.put(lexemes[1], task);
                            }
                        }
                        case "delete" -> {
                            if (lexemes.length == 1) System.err.format("Must be two words");
                            completeTasks.remove(lexemes[1]);
                            incompleteTasks.remove(lexemes[1]);
                        }
                        case "print" -> {
                            PrintStream out = System.out;
                            if (lexemes.length == 3) out = new PrintStream(new FileOutputStream(lexemes[2]));
                            print(to.get(lexemes[1]), out, false);
                        }
                        default -> System.err.format("Unsupported operation : %s\n", lexemes[0]);
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    System.err.println("Must be more words");
                }
            }
        } catch (IOException e) {
            System.err.println("Problems with input :");
            e.printStackTrace();
        } catch (NullPointerException ignored) {}
    }

    public ToDoList() {
        completeTasks = new HashMap<>();
        incompleteTasks = new HashMap<>();
    }

    public static void main(String[] args) {
        new ToDoList().run();
    }
}

import java.io.*;
import java.nio.file.InvalidPathException;
import java.util.HashMap;

public class ToDoList {
    HashMap<String, String> uncompletedTasks, completedTasks;

    void print(String arg, PrintStream out) {
        switch (arg) {
            case "uncompleted" -> {
                out.println("Uncompleted :");
                uncompletedTasks.forEach((label, task) -> out.format("\t%s : %s\n", label, task));
            }
            case "completed" -> {
                out.println("Completed : ");
                completedTasks.forEach((label, task) -> out.format("\t%s : %s\n", label, task));
            }
            default -> System.err.format("Unsupported label - %s\n", arg);
        }
    }

    void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            String[] lexemes;
            while ((line = in.readLine()) != null) {
                lexemes = line.split(" ");
                try {
                    switch (lexemes[0]) {
                        case "load" -> {
                        }
                        case "unload" -> {
                            PrintStream out = new PrintStream(new FileOutputStream(lexemes[1]));
                            print("uncompleted",out);
                            print("completed",out);
                        }
                        case "add" -> {
                        }
                        case "mark" -> {

                        }
                        case "delete" -> {
                        }
                        case "print" -> {
                            if (lexemes.length == 1) System.err.format("Must be two words");
                            else if (lexemes[1].equals("all")) {
                                print("uncompleted",System.out);
                                print("completed",System.out);
                            } else print(lexemes[1],System.out);
                        }
                        default -> System.err.format("Unsupported operation : %s\n", lexemes[0]);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.format("must be more words\n");
                } catch (InvalidPathException e) {
                    System.err.format("Incorrect path : %s \n %s\n", lexemes[1], e.getReason());
                }
            }
        } catch (IOException e) {
            System.err.println("Problems with input :");
            e.printStackTrace();
        }
    }

    public ToDoList() {
        completedTasks = new HashMap<>();
        uncompletedTasks = new HashMap<>();
    }

    public static void main(String[] args) {
        new ToDoList().run();
    }
}

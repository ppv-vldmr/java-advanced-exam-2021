import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.InvalidPathException;
import java.util.HashMap;

public class ToDoList {
    HashMap<String, String> uncompletedTasks, completedTasks;

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
                        }
                        case "add" -> {
                        }
                        case "mark" -> {
                        }
                        case "delete" -> {
                        }
                        case "print" -> {
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

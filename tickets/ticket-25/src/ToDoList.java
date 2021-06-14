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

    void print(MODE arg, PrintStream out, PrintStream err, boolean mark) {
        switch (arg) {
            case INCOMPLETE -> incompleteTasks.forEach((label, task) -> out.format("* %s : %s\n", label, task));
            case COMPLETE -> completeTasks.forEach((label, task) -> out.format(mark ? "* ~~%s : %s~~\n" : "* %s : %s\n", label, task));
            case ALL -> {
                print(MODE.COMPLETE, out, err,true);
                print(MODE.INCOMPLETE, out, err,true);
            }
            default -> err.format("Unsupported label - %s\n", arg);
        }
    }

    public void run(InputStream inputStream, PrintStream out, PrintStream err) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
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
                                        String task = Arrays.stream(words).skip(2).collect(Collectors.joining(" "));
                                        if (record.endsWith("~~")) completeTasks.put(words[1], task);
                                        else incompleteTasks.put(words[1], task);
                                    }
                                }
                            } else {
                                incompleteTasks.put(lexemes[1], Arrays.stream(lexemes).skip(2).collect(Collectors.joining(" ")));
                            }

                        }
                        case "mark" -> {
                            if (lexemes.length == 1) err.format("Must be two words");
                            String task;
                            if ((task = incompleteTasks.remove(lexemes[1])) != null) {
                                completeTasks.put(lexemes[1], task);
                            }
                        }
                        case "delete" -> {
                            if (lexemes.length == 1) err.format("Must be two words");
                            completeTasks.remove(lexemes[1]);
                            incompleteTasks.remove(lexemes[1]);
                        }
                        case "print" -> {
                            PrintStream tempOut = out;
                            if (lexemes.length == 3) tempOut = new PrintStream(new FileOutputStream(lexemes[2]));
                            print(to.get(lexemes[1]), tempOut, err, false);
                        }
                        default -> err.format("Unsupported operation : %s\n", lexemes[0]);
                    }
                }catch (ArrayIndexOutOfBoundsException e){
                    err.println("Must be more words");
                }
            }
        } catch (IOException e) {
            err.println("Problems with input :");
            e.printStackTrace();
        } catch (NullPointerException ignored) {}
    }

    public ToDoList() {
        completeTasks = new HashMap<>();
        incompleteTasks = new HashMap<>();
    }

    public static void main(String[] args) {
        new ToDoList().run(System.in,System.out,System.err);
    }
}

import java.io.*;

public class Main {

    public static void main(String[] args){
        try {
            run(args);
        } catch (TabulatorException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void run(String[] args) throws TabulatorException{
        if (args == null){
            throw new TabulatorException("Null arguments");
        }
        if (args.length != 1) {
            throw new TabulatorException("Incorrect number of arguments");
        }
        String filename = args[0];
        File file = new File(filename);
        File newFile = new File("new" + filename);
        try (BufferedReader input = new BufferedReader(new FileReader(file))) {
            try (BufferedWriter output = new BufferedWriter(new FileWriter(newFile))) {
                int c;
                while ((c = input.read()) != -1) {
                    if (c == 9) {
                        for (int i = 0; i < 4; ++i)
                            output.write(" ");
                    } else {
                        output.write(c);
                    }
                }
            } catch (IOException e){
                throw new TabulatorException("Impossible to write file");
            }
        } catch (FileNotFoundException e) {
            throw new TabulatorException("File not found");
        } catch (IOException e) {
            throw new TabulatorException("Impossible to read file");
        }
        int i = filename.lastIndexOf('.');
        String name = filename.substring(0, i);
        File file2 = new File(name + ".backup");
        file.renameTo(file2);
        newFile.renameTo(new File(filename));
        file2.delete();
    }
}
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Objects;

public class FileManager {
    Path currentDir;

    private static final SimpleFileVisitor<Path> DELETE = new SimpleFileVisitor<>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return super.visitFile(file, attrs);
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return super.postVisitDirectory(dir, exc);
        }
    };

    private void dir(PrintStream out, PrintStream err) {
        try {
            out.format("Current dir : %s\n",currentDir.toString());
            Arrays.stream(Objects.requireNonNull(currentDir.toFile().list())).forEach(out::println);
        } catch (NullPointerException e) {
            err.format("Error occurred : \n%s\n", e.getMessage());
        }
    }

    void rm(Path path, PrintStream out, PrintStream err) {
        try {
            Files.delete(currentDir.resolve(path));
            out.format("File %s deleted\n",path.toString());
        } catch (NoSuchFileException e) {
            err.format("File %s doesn't exist : \n%s\n", path.toString(), e.getMessage());
        } catch (DirectoryNotEmptyException e) {
            err.format("%s is directory : \n%s\n", path.toString(), e.getMessage());
        } catch (IOException e) {
            err.format("I/O error with file %s : \n%s\n", path.toString(), e.getCause());
        }
    }

    void cd(Path path, PrintStream out, PrintStream err) {
        if (Files.exists(currentDir.resolve(path))) {
            if (Files.isDirectory(currentDir.resolve(path))) {
                currentDir = currentDir.resolve(path).normalize();
                out.format("Current directory changed to %s\n",path.toString());
            } else {
                err.format("%s isn't directory\n", path.toString());
            }
        } else {
            err.format("Directory %s doesn't exist\n", path.toString());
        }
    }

    void create(Path path, PrintStream out, PrintStream err) {
        try {
            Files.createFile(currentDir.resolve(path));
            out.format("File %s created\n",path.toString());
        } catch (FileAlreadyExistsException e) {
            err.format("File/directory %s already exists : \n%s\n", path.toString(), e.getReason());
        } catch (IOException e) {
            err.format("Parent directory of %s doesn't exist : \n%s\n", path.toString(), e.getCause());
        }
    }

    void mkdir(Path path, PrintStream out, PrintStream err) {
        try {
            Files.createDirectory(currentDir.resolve(path));
            out.format("Directory %s created\n",path.toString());
        } catch (FileAlreadyExistsException e) {
            err.format("File/directory %s already exists : \n%s\n", path.toString(), e.getReason());
        } catch (IOException e) {
            err.format("Parent directory of %s doesn't exist : \n%s\n", path.toString(), e.getCause());
        }
    }

    void rmdir(Path path, PrintStream out, PrintStream err) {
        try {
            Files.walkFileTree(currentDir.resolve(path), DELETE);
            out.format("Directory %s deleted\n",path.toString());
        } catch (IOException e) {
            err.println(e.getMessage());
        }
    }

    public void run(InputStream inputStream, PrintStream out, PrintStream err) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            String[] lexemes;
            while ((line = in.readLine()) != null) {
                lexemes = line.split(" ");
                try {
                    switch (lexemes[0]) {
                        case "dir" -> dir(out, err);
                        case "cd" -> cd(Paths.get(lexemes[1]), out, err);
                        case "rm" -> rm(Paths.get(lexemes[1]), out, err);
                        case "create" -> create(Paths.get(lexemes[1]), out, err);
                        case "mkdir" -> mkdir(Paths.get(lexemes[1]), out, err);
                        case "rmdir" -> rmdir(Paths.get(lexemes[1]), out, err);
                        default -> err.format("Unsupported operation : %s\n", lexemes[0]);
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    err.format("must be more words\n");
                } catch (InvalidPathException e) {
                    err.format("Incorrect path : %s \n %s\n", lexemes[1], e.getMessage());
                }
            }
        } catch (IOException e) {
            err.format("Problems with input : \n %s", e.getMessage());
        }
    }

    public FileManager(Path source) {
        currentDir = source;
    }

    public static void main(String[] args) {
        new FileManager(Path.of(System.getProperty("user.dir"))).run(System.in, System.out, System.err);
    }
}

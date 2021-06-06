import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Objects;

public class FileManager {
    Path currentDir;

    private static class DeleteVisitor extends SimpleFileVisitor<Path> {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return super.visitFile(file, attrs);
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            Files.delete(file);
            return super.visitFileFailed(file, exc);
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return super.postVisitDirectory(dir, exc);
        }
    }

    void dir() {
        try {
            Arrays.stream(Objects.requireNonNull(currentDir.toFile().list())).forEach(System.out::println);
        } catch (NullPointerException e){
            System.err.format("Error occured : \n%s\n",e.getMessage());
        }
    }

    void rm(Path path) {
        try {
            Files.delete(path);
        } catch (NoSuchFileException e) {
            System.err.format("File %s doesn't exist : \n%s\n", path.toString(), e.getCause());
        } catch (DirectoryNotEmptyException e) {
            System.err.format("%s is directory : \n%s\n", path.toString(), e.getCause());
        } catch (IOException e) {
            System.err.format("I/O error with file %s : \n%s\n", path.toString(), e.getCause());
        }
    }

    void cd(Path path) {
        if (Files.exists(currentDir.resolve(path))) {
            if (Files.isDirectory(currentDir.resolve(path))) {
                currentDir = currentDir.resolve(path);
            } else {
                System.err.format("%s isn't directory\n", path.toString());
            }
        } else {
            System.err.format("Directory %s doesn't exist\n", path.toString());
        }
    }

    void create(Path path) {
        try {
            Files.createFile(currentDir.resolve(path));
        } catch (FileAlreadyExistsException e) {
            System.err.format("File/directory %s already exists : \n%s\n", path.toString(), e.getReason());
        } catch (IOException e) {
            System.err.format("Parent directory of %s doesn't exist : \n%s\n", path.toString(), e.getCause());
        }
    }

    void mkdir(Path path) {
        try {
            Files.createDirectory(currentDir.resolve(path));
        } catch (FileAlreadyExistsException e) {
            System.err.format("File/directory %s already exists : \n%s\n", path.toString(), e.getReason());
        } catch (IOException e) {
            System.err.format("Parent directory of %s doesn't exist : \n%s\n", path.toString(), e.getCause());
        }
    }

    void rmdir(Path path) {
        try {
            Files.walkFileTree(currentDir.resolve(path), new DeleteVisitor());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            String[] lexemes;
            while ((line = in.readLine()) != null) {
                lexemes = line.split(" ");
                try {
                    switch (lexemes[0]) {
                        case "dir" -> dir();
                        case "cd" -> cd(Paths.get(lexemes[1]));
                        case "rm" -> rm(Paths.get(lexemes[1]));
                        case "create" -> create(Paths.get(lexemes[1]));
                        case "mkdir" -> mkdir(Paths.get(lexemes[1]));
                        case "rmdir" -> rmdir(Paths.get(lexemes[1]));
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

    public FileManager(Path source) {
        currentDir = source;
    }

    public static void main(String[] args) {
        new FileManager(Path.of(System.getProperty("user.dir"))).run();
    }
}

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.stream.Collectors;


public class ToDoListTest {
    private SimpleFileVisitor<Path> DELETE = new SimpleFileVisitor<>() {
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


    @Test
    public void testRun() {
        ToDoList toDoList = new ToDoList();
        Path expected = Path.of("expected"), actual = Path.of("actual");
        try {
            Files.createDirectories(Path.of("actual"));
            toDoList.run(
                    new FileInputStream("tests/script"),
                    new PrintStream(new FileOutputStream(actual.resolve("actual.md").toFile())),
                    System.err);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Path[] files = new Path[]{
                Path.of("actual.md"),
                Path.of("complete.md"),
                Path.of("incomplete.md"),
                Path.of("all.md")
        };
        for(Path out : files) {
            try {
                List<String> ex = Files.lines(expected.resolve(out)).collect(Collectors.toList()),
                            ac = Files.lines(actual.resolve(out)).collect(Collectors.toList());
                Assertions.assertEquals(ex,ac);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Files.walkFileTree(actual,DELETE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

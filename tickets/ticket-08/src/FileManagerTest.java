import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

class FileManagerTest {
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

    @Test
    void testRun() throws IOException {
        Path root = Path.of("test");
        Files.createDirectory(root);
        Files.createFile(root.resolve("test_file"));
        Files.createDirectory(root.resolve("test-dir"));
        FileManager manager = new FileManager(root);
        PrintStream realityOutput = new PrintStream("out");
        manager.run(new FileInputStream("script"), realityOutput, realityOutput);
        realityOutput.close();
        ArrayList<String> reality = new ArrayList<>();
        Files.lines(Path.of("out")).forEach(reality::add);
        ArrayList<String> expected = new ArrayList<>();

        expected.add("Current dir : test"                        );
        expected.add("test_file"                                 );
        expected.add("test-dir"                                  );
        expected.add("File test_file deleted"                    );
        expected.add("Current directory changed to test-dir"     );
        expected.add("Current dir : test/test-dir"               );
        expected.add("File aaaa created"                         );
        expected.add("Current dir : test/test-dir"               );
        expected.add("aaaa"                                      );
        expected.add("Current directory changed to .."           );
        expected.add("Directory test-dir deleted"                );
        expected.add("Current dir : test"                        );

        Files.walkFileTree(root, DELETE);
        Files.delete(Path.of("out"));
        Assertions.assertEquals(expected, reality);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
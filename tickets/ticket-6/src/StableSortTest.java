import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

class StableSortTest {
    @Test
    void testMain() throws IOException {
        Path fileTest = Path.of("test.in");
        Files.createFile(fileTest);
        BufferedWriter fileWriter = Files.newBufferedWriter(fileTest);
        String[] commands = {
                "print",
                "add 1 ccc",
                "add 2 bb",
                "add 0 aaa",
                "add 1 aaa",
                "print"};
        for(String comm : commands) fileWriter.write(comm+"\n");
        fileWriter.close();
        StableSort.main(new String[]{"test.in","test.out"});
        Stream<String> reality = Files.lines(Path.of("test.out"));

    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme
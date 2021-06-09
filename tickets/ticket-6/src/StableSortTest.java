import org.junit.Assert;
import org.junit.jupiter.api.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
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
        Files.delete(fileTest);
        ArrayList<String> realityList = new ArrayList<>();
        Files.lines(Path.of("test.out")).forEach(realityList::add);
        String[] expected = new String[]{
                "-----START-----",
                "------END------",
                "-----START-----",
                "0 aaa",
                "1 ccc",
                "1 aaa",
                "2 bb",
                "------END------"
        };
        Assertions.assertArrayEquals(realityList.toArray(),expected);
    }
}
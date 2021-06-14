import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class SortTest {
    static String[] funnyStrings = new String[]{
            "\t\t\t\t\taaaaaaart",
            "333aa444",
            "## Билет 29. Sort"
            , "Напишите аналог утилиты `sort`."
            , "### Функциональность"
            , "- Поддерживаемые опции:"
            , "    * `--ignore-leading-blanks`"
            , "    * `--dictionary-order     `"
            , "    * `--ignore-case          `"
            , "    * `--general-numeric-sort `"
            , "    * `--ignore-nonprinting   `"
            , "    * `--numeric-sort         `"
            , "    * `--reverse              `"
            , "- Ввод данных с консоли или входного файла."
            , "- Исходные данные могут __не поместиться__ в памяти."
            , "### Технологии"
            , "- I/O"
            , "- Collections Framework"
    };

    @BeforeAll
    static void beforeAll() throws FileNotFoundException {
        PrintStream test1 = new PrintStream(new FileOutputStream("tests/test1"));
        IntStream.range(0, 1<<10).forEach(
                s -> test1.println( Math.random() > 0.5 ? s : funnyStrings[s%funnyStrings.length])
        );
        test1.close();
    }

    @Test
    void main() throws IOException {
        Sort.main(new String[]{
                "in=tests/test1",
                "out=actual.out"
        });
    }
}
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class SortTest {
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
            , "- Collections Framework",
            "## Билет 25. TODO List",
            "    Сделать приложение, реализующее список задач.",
            "            ### Функциональность ",
            "- Загрузка списка задач из файла.",
            "- Запись списка задач в файл.",
            "- Операции с задачами   ",
            "    * добавление задачи;",
            "    * удаление задачи;  ",
            "    * пометка задачи как выполненной.",
            "            - Просмотр задач:",
            "            * всех;          ",
            "    * выполненных;           ",
            "    * невыполненных.         ",
            "- Интерактивный ввод-ввод осуществляется с консоли.",
            "            ### Технологии       ",
            "- I/O                            ",
            "- Collections Framework          ",
            "---------------------------------",
            "    Синтаксис корректного файла для загрузки: `(<label> <string>\n)*`              ",
            "            - Операции с задачами                                                  ",
            "    * `add <label> <task>` - добавление задачи с меткой `<label>` и текстом задачи;",
            "    * `delete <label>` - удаление задачи с меткой `<label>`, если таковая имеется; ",
            "    * `mark <label>` - пометка задачи с меткой `<label>` как выполненной, если таковая имеется.",
            "            - Просмотр задач - `print <label>`",
            "            * всех - `<label> = all`;         ",
            "    * выполненных - `<label> = complete`;     ",
            "    * невыполненных - `<label = uncomplete`.  ",

    };

    @BeforeAll
    static void beforeAll() throws FileNotFoundException {
        PrintStream test1 = new PrintStream(new FileOutputStream("tests/test1"));
        IntStream.range(0, 1 << 10).forEach(
                s -> test1.println(Math.random() > 0.5 ? s : funnyStrings[s % funnyStrings.length])
        );
        test1.close();
        PrintStream test2 = new PrintStream(new FileOutputStream("tests/test2"));
        IntStream.range(0, 1 << 10).forEach(s -> test2.println(Math.random()));
        test2.close();
        PrintStream test3 = new PrintStream(new FileOutputStream("tests/test3"));
        IntStream.range(0, 1 << 10).forEach(s -> test3.format("%f",Math.random()));
        test3.close();
        PrintStream test4 = new PrintStream(new FileOutputStream("tests/test4"));
        IntStream.range(0, 1 << 10).forEach(s -> test4.format("%f",Math.random()));
        test3.close();
    }

    boolean equal(Path file1, Path file2) {
        String str1;
        boolean ans = false;
        try(BufferedReader in1 = Files.newBufferedReader(file1)) {
            try(BufferedReader in2 = Files.newBufferedReader(file2)) {
                while (Objects.equals((str1 = in1.readLine()), in2.readLine())) {
                    if(str1 == null) {
                        ans = true;
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ans;
    }

    @Test
    void test1() throws IOException {
        Sort.main(new String[]{"in=tests/test1","out=actual1.out"});
        Runtime.getRuntime().exec("sort tests/test1 > expected1.out");
        assertTrue(equal(Path.of("expected1.out"),Path.of("actual1.out")));
    }

    @AfterAll
    static void afterAll() {

    }
}
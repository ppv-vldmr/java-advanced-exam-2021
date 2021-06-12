import element.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;

public class Tests {
    private final static Function<String, IntElement> intParser = (String input) -> new IntElement(Integer.parseInt(input));
    private static final Function<String, LongElement> longParser = (String input) -> new LongElement(Long.parseLong(input));
    private static final Function<String, DoubleElement> doubleParser = (String input) -> new DoubleElement(Double.parseDouble(input));
    private static final Function<String, BigIntegerElement> bigIntegerParser = (String input) -> new BigIntegerElement(new BigInteger(input));
    private static final Function<String, BigDecimalElement> bigDecimalParser = (String input) -> new BigDecimalElement(new BigDecimal(input));

    @Test()
    public void test1_creatingMatrix() {
        System.err.println("Running test1_creatingMatrix...");
        IntElement[][] values = new IntElement[100][100];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                values[i][j] = new IntElement(i + j);
            }
        }
        Matrix<Integer> matrix = new Matrix<>(values);
        for (int i = 0; i < matrix.rows; i++) {
            for (int j = 0; j < matrix.columns; j++) {
                int element = matrix.get(i, j).get();
                Assert.assertEquals(element, i + j);
            }
        }
        System.err.println("Test1 passed!");
    }

    @Test
    public void test2_differentTypes() {
        System.err.println("Running test2_differentTypes");
        LongElement[][] longValues = new LongElement[10][10];
        DoubleElement[][] doubleValues = new DoubleElement[10][10];
        BigIntegerElement[][] bigIntegerValues = new BigIntegerElement[10][10];
        BigDecimalElement[][] bigDecimalValues = new BigDecimalElement[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                longValues[i][j] = new LongElement(1000000000L * (i + j));
                doubleValues[i][j] = new DoubleElement(0.01 * (i + j));
                bigIntegerValues[i][j] = new BigIntegerElement(new BigInteger("1000" + "1000".repeat(i + j)));
                bigDecimalValues[i][j] = new BigDecimalElement(new BigDecimal("0.1" + "01".repeat(i + j)));
            }
        }
        Matrix<Long> matrixLong = new Matrix<>(longValues);
        Matrix<Double> matrixDouble = new Matrix<>(doubleValues);
        Matrix<BigInteger> matrixBigInteger = new Matrix<>(bigIntegerValues);
        Matrix<BigDecimal> matrixBigDecimal = new Matrix<>(bigDecimalValues);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                long one = matrixLong.get(i, j).get();
                Assert.assertEquals(one, 1000000000L * (i + j));
                double two = matrixDouble.get(i, j).get();
                Assert.assertEquals(two, 0.01 * (i + j), 0.001);
                BigInteger three = matrixBigInteger.get(i, j).get();
                Assert.assertEquals(three, new BigInteger("1000" + "1000".repeat(i + j)));
                BigDecimal four = matrixBigDecimal.get(i, j).get();
                Assert.assertEquals(four, new BigDecimal("0.1" + "01".repeat(i + j)));
            }
        }
        System.err.println("Test2 passed");
    }

    @Test
    public void test3_transpose() {
        System.err.println("Running test3_transpose...");
        Element<Integer>[][] values = new IntElement[500][600];
        for (int i = 0; i < 500; i++) {
            for (int j = 0; j < 600; j++) {
                values[i][j] = new IntElement(0);
            }
        }
        values[100][550].set(-1);
        Matrix<Integer> matrix = new Matrix<>(values);
        for (int i = 0; i < 10000000; i++) {
            matrix.transpose();
            if (i % 2 == 0) {
                int value = matrix.get(550, 100).get();
                Assert.assertEquals(-1, value);
            } else {
                int value = matrix.get(100, 550).get();
                Assert.assertEquals(-1, value);
            }
        }
        System.err.println("Test3 passed!");
    }

    @Test
    public void test4_add() {
        System.err.println("Running test4_add");
        Element<Integer>[][] values1 = new IntElement[100][50];
        Element<Integer>[][] values2 = new IntElement[100][50];
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 50; j++) {
                values1[i][j] = new IntElement(i + 3 * j);
                values2[i][j] = new IntElement(4 * i + 2 * j);
            }
        }
        Matrix<Integer> matrix1 = new Matrix<>(values1);
        Matrix<Integer> matrix2 = new Matrix<>(values2);
        Matrix<Integer> matrix = matrix1.add(matrix2);
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 50; j++) {
                int element = matrix.get(i, j).get();
                Assert.assertEquals(i * 5 + j * 5, element);
            }
        }
        System.err.println("Test4 passed!");
    }

    @Test
    public void test5_multiplyInverse() {
        System.err.println("Running test5_multiplyInverse");
        Element<Integer>[][] original = new IntElement[5][5];
        Element<Integer>[][] inverse = new IntElement[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == j || i + 1 == j) {
                    original[i][j] = new IntElement(1);
                } else {
                    original[i][j] = new IntElement(0);
                }
                if (i <= j) {
                    if ((j - i) % 2 == 0) {
                        inverse[i][j] = new IntElement(1);
                    } else {
                        inverse[i][j] = new IntElement(-1);
                    }
                } else {
                    inverse[i][j] = new IntElement(0);
                }
            }
        }
        Matrix<Integer> originalMatrix = new Matrix<>(original);
        Matrix<Integer> inverseMatrix = new Matrix<>(inverse);
        Matrix<Integer> one = originalMatrix.multiply(inverseMatrix);
        Matrix<Integer> two = inverseMatrix.multiply(originalMatrix);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                int first = one.get(i, j).get();
                int second = two.get(i, j).get();
                if (i == j) {
                    Assert.assertEquals(1, first);
                    Assert.assertEquals(1, second);
                } else {
                    Assert.assertEquals(0, first);
                    Assert.assertEquals(0, second);
                }
            }
        }
        System.err.println("Test5 passed!");
    }

    @Test
    public void test6_multiplyDifferentSizes() {
        System.err.println("Running test6_multiplyDifferentSizes");
        Element<Integer>[][] firstValues = new IntElement[3][5];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == 0 || i == 2) {
                    firstValues[i][j] = new IntElement(1);
                } else {
                    firstValues[i][j] = new IntElement(0);
                }
            }
        }
        Element<Integer>[][] secondValues = new IntElement[5][4];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 4; j++) {
                if (j == 0 || j == 3) {
                    secondValues[i][j] = new IntElement(1);
                } else {
                    secondValues[i][j] = new IntElement(0);
                }
            }
        }
        Matrix<Integer> first = new Matrix<>(firstValues);
        Matrix<Integer> second = new Matrix<>(secondValues);
        Matrix<Integer> result = first.multiply(second);
        Assert.assertEquals(3, result.rows);
        Assert.assertEquals(4, result.columns);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j< 4; j++) {
                int element = result.get(i, j).get();
                if (i == 0 && j == 0 || i == 2 && j == 0 || i == 0 && j == 3 || i == 2 && j == 3) {
                    Assert.assertEquals(5, element);
                } else {
                    Assert.assertEquals(0, element);
                }
            }
        }
        System.err.println("Test6 passed");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void test7_readingMatrixFromFile() {
        System.err.println("Running test7_readingMatrixFromFile");
        Path path = Path.of("test.txt");
        try (BufferedWriter out = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    out.append(Integer.toString(i + j));
                    out.write(" ");
                }
                out.newLine();
            }
        } catch (final IOException e) {
            throw new RuntimeException("Sosy");
        }
        try {
            Matrix<Integer> matrix = new Matrix(path, intParser, 100, 100);
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    int element = matrix.get(i, j).get();
                    Assert.assertEquals(i + j, element);
                }
            }
        } catch (MatrixException e) {
            throw new RuntimeException("Problems while running " + e.getMessage());
        }
        System.err.println("Test7 passed!");
    }

    @Test
    public void test8_write() {
        Path path = Path.of("tests\\test.txt");
        try {
            Element<Integer>[][] values = new IntElement[100][100];
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    values[i][j] = new IntElement(i + j);
                }
            }
            Matrix<Integer> matrix = new Matrix<>(values);
            matrix.write(path);
        } catch (final MatrixException e) {
            throw new RuntimeException("Can't write " + e.getMessage());
        }
        try {
            Matrix<Integer> matrix = new Matrix(path, intParser, 100, 100);
            for (int i = 0; i < 100; i++) {
                for (int j = 0; j < 100; j++) {
                    int element = matrix.get(i, j).get();
                    Assert.assertEquals(i + j, element);
                }
            }
        } catch (final MatrixException e) {
            System.err.println(e.getMessage());
        }
    }
}

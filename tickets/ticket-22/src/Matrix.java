import element.Element;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.IntStream;

public class Matrix<T extends Number> {
    private final Element<T>[][] values;
    public int rows;
    public int columns;
    private boolean transposed;

    @SafeVarargs
    public Matrix(Element<T>[]... values) {
        rows = values.length;
        assert rows != 0;
        columns = values[0].length;
        for (final Element<T>[] row : values) {
            if (row.length != columns) {
                throw new IllegalArgumentException("Incorrect matrix!");
            }
        }
        this.values = values;
        transposed = false;
    }

    @SuppressWarnings("unchecked")
    public Matrix(final Path path, final Function<String, Element<T>> parser, final int rows, final int columns) throws MatrixException {
        try (Scanner scanner = new Scanner(path, parser)) {
            this.rows = rows;
            this.columns = columns;
            transposed = false;
            values = new Element[rows][columns];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    values[i][j] = scanner.next();
                }
            }
        } catch (final IOException e) {
            throw new MatrixException("Can't read matrix", e);
        }
    }

    public void transpose() {
        transposed = !transposed;
        int temporary = columns;
        columns = rows;
        rows = temporary;
    }


    public Element<T> get(final int i, final int j) {
        if (i >= rows) {
            throw new IndexOutOfBoundsException("Trying to get row " + i + ", but number of rows is " + rows);
        } else if (j >= columns) {
            throw new IndexOutOfBoundsException("Trying to get column " + j + ", but number of columns is " + columns);
        }
        int x = transposed ? j : i;
        int y = transposed ? i : j;
        return values[x][y];
    }

    @SuppressWarnings("unchecked")
    public Matrix<T> add(final Matrix<T> other) {
        assert (other.rows == rows && other.columns == columns);
        Element<T>[][] matrix = new Element[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = get(i, j).add(other.get(i, j));
            }
        }
        return new Matrix<T>(matrix);
    }

    @SuppressWarnings("unchecked")
    public Matrix<T> multiply(final Matrix<T> other) {
        assert columns == other.rows;
        Element<T>[][] matrix = new Element[rows][other.columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < other.columns; j++) {
                Element<T> element = get(i, 0).multiply(other.get(0, j));
                for (int k = 1; k < columns; k++) {
                    element = element.add(get(i, k).multiply(other.get(k, j)));
                }
                matrix[i][j] = element;
            }
        }
        return new Matrix<>(matrix);
    }

    public void write(final Path path) throws MatrixException {
        directory(path);
        try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    writer.write(get(i, j).get().toString());
                    writer.write(" ");
                }
                writer.write(Character.LINE_SEPARATOR);
            }
        } catch (final IOException e) {
            throw new MatrixException("Can't write", e);
        }
    }

    private void directory(final Path out) {
        final Path directory = out.getParent();
        if (directory != null && !Files.isDirectory(directory)) {
            try {
                Files.createDirectory(directory);
            } catch (final IOException e) {
                System.out.println("Can't create directory!");
            } catch (final SecurityException e) {
                System.out.println("Problems with rights!");
            }
        }
    }

    private class Scanner implements AutoCloseable {
        private final Reader in;
        private int symbol;
        private final Function<String, Element<T>> parser;

        Scanner(final Path path, final Function<String, Element<T>> function) throws IOException {
            in = Files.newBufferedReader(path);
            parser = function;
            symbol = in.read();
        }

        Scanner(final String string, final Function<String, Element<T>> function) throws IOException {
            in = new StringReader(string);
            parser = function;
            symbol = in.read();
        }

        boolean checkNumber(final char symbol) {
            return (Character.isDigit(symbol) || symbol == Character.DASH_PUNCTUATION || symbol == '.');
        }

        boolean hasNext() throws IOException {
            while (symbol != -1 && !checkNumber((char) symbol)) {
                symbol = in.read();
            }
            return symbol != -1;
        }

        Element<T> next() throws IOException {
            if (!hasNext()) {
                throw new IOException("No next element!");
            }
            StringBuilder builder = new StringBuilder();
            while (symbol != -1 && checkNumber((char) symbol)) {
                builder.append((char) symbol);
                symbol = in.read();
            }
            return parser.apply(builder.toString());
        }

        @Override
        public void close() throws IOException {
            in.close();
        }
    }
}

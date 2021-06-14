package core;

import core.Core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RemoteCore implements Core {
    private final static int QUERIES_MAX = 1000;

    private final List<String> rows = new ArrayList<>();
    private final List<String> queries = new ArrayList<>();

    private static void createParentDirectories(Path file) throws IOException {
        Path parent;
        if ((parent = file.getParent()) == null) {
            return;
        }
        Files.createDirectories(parent);
    }

    public synchronized void read(Path input) throws IOException {
        createParentDirectories(input);
        try (var reader = Files.newBufferedReader(input)) {
            add(reader.readLine());
        }
    }

    public synchronized void write(Path output) throws IOException {
        createParentDirectories(output);
        try (var writer = Files.newBufferedWriter(output)) {
            List<String> rows = list();
            for (String row : rows) {
                writer.write(row);
                writer.newLine();
            }
        }
    }

    private void addQuery(String query) {
        if (queries.size() == QUERIES_MAX) {
            queries.subList(0, QUERIES_MAX / 2).clear();
        }
        queries.add(query);
    }

    private void logAndAddQuery(String query) {
        System.out.println(query);
        addQuery(query);
    }

    @Override
    public synchronized void add(String row) {
        logAndAddQuery("add " + row);
        if (row == null) {
            throw new NullPointerException("Row can not be null");
        }
        rows.add(row);
    }

    @Override
    public synchronized int remove(String row) {
        logAndAddQuery("remove " + row);
        if (row == null) {
            return 0;
        }
        int count = 0;
        for (var iterator = rows.iterator(); iterator.hasNext();) {
            if (iterator.next().equals(row)) {
                ++count;
                iterator.remove();
            }
        }
        return count;
    }

    @Override
    public synchronized void removeByIndex(int index) {
        logAndAddQuery("remove " + index);
        if (index < 0 || index >= rows.size()) {
            throw new IndexOutOfBoundsException("index must be >= 0 and < rowsCount, but was " + index);
        }
        rows.remove(index);
    }

    @Override
    public synchronized List<String> list() throws RemoteException {
        logAndAddQuery("list");
        return IntStream.range(0, rows.size()).mapToObj(i -> i + " " + rows.get(i)).collect(Collectors.toList());
    }

    @Override
    public synchronized List<Boolean> contains(List<String> list) throws RemoteException {
        logAndAddQuery("contains " + list);
        return list.stream().map(rows::contains).collect(Collectors.toList());
    }

    @Override
    public synchronized List<String> queries() throws RemoteException {
        logAndAddQuery("queries");
        return Collections.unmodifiableList(queries);
    }
}

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.function.Consumer;

public class Sort {
    private static final int BUFFER_SIZE = 128;
    private static final Map<String,Comparator<String>> mapToComparator = new HashMap<>();

    static {
        mapToComparator.put("--ignore-case",String::compareToIgnoreCase);
        mapToComparator.put("--dictionary-order",Comparator.comparing(s -> s.replaceAll("[^\\da-zA-Zа-яёА-ЯЁ ]", "")));
        mapToComparator.put("--ignore-leading-blanks", Comparator.comparing(String::stripLeading));
        mapToComparator.put("--ignore-non-printing",Comparator.comparing(s -> s.replaceAll("[^\\p{ASCII}]", "")));
        mapToComparator.put("--numeric-sort" ,Comparator.comparingLong(Long::parseLong));
        mapToComparator.put("--general-numeric-sort",Comparator.comparingDouble(Double::valueOf));
    }

    static Comparator<? super String> takeComparator(ArrayList<String> options) {
        // TODO пересмотрите эту функцию, почти наверняка я где-то набагал
        Comparator<String> ans = Comparator.naturalOrder();

        return options.contains("--reverse") ? ans.reversed() : ans;
    }


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

    Path make_name(Path folder, int num) {
        return folder.resolve(Integer.toString(num));
    }

    void merge(BufferedReader[] ins, PrintStream out, Comparator<? super String> comp) throws IOException {
        Comparator<String> nullComparator = Comparator.nullsLast(comp);
        String[] strs = {ins[0].readLine(), ins[1].readLine()};
        Consumer<Integer> next = (Integer ind) -> {
            out.println(strs[ind]);
            try {
                strs[ind] = ins[ind].readLine();
            } catch (IOException e) {
                strs[ind] = null;
            }
        };
        while (!Arrays.stream(strs).allMatch(Objects::isNull)) {
            switch (Integer.signum(nullComparator.compare(strs[0], strs[1]))) {
                case -1 -> next.accept(0);
                case 0 -> {
                    next.accept(0);
                    next.accept(1);
                }
                case 1 -> next.accept(1);
            }
        }
        ins[0].close();
        ins[1].close();
        out.close();
    }

    Sort(Comparator<? super String> comp, BufferedReader in, PrintStream out, PrintStream err) {
        Path folder = Path.of(System.getProperty("user.dir")).resolve(".sorting");
        ArrayDeque<Path> files;
        try {
            Files.createDirectories(folder);
            files = cut(in, folder, comp);
            int current = files.size();
            while (files.size() >= 2) {
                Path file1 = files.poll(), file2 = files.poll(), tempOut = make_name(folder, current++);
                files.add(tempOut);
                PrintStream tempPrintStream = new PrintStream(new FileOutputStream(tempOut.toFile()));
                assert file2 != null;
                merge(new BufferedReader[]{
                        Files.newBufferedReader(file1),
                        Files.newBufferedReader(file2)
                }, tempPrintStream, comp);
                Files.delete(file1);
                Files.delete(file2);
            }
            BufferedReader outReader = Files.newBufferedReader(Objects.requireNonNull(files.poll()));
            String str;
            while ((str = outReader.readLine()) != null)
                out.println(str);
            in.close();
            err.close();
            out.close();
            Files.walkFileTree(folder,DELETE);
        } catch (IOException e) {
            err.println(e.getMessage());
        }
    }

    ArrayDeque<Path> cut(BufferedReader in, Path folder, Comparator<? super String> comp) throws IOException {
        String str;
        ArrayDeque<Path> files = new ArrayDeque<>();
        int currentFile = 0, stringsInFile = 0;
        BufferedWriter out = Files.newBufferedWriter(folder.resolve(Integer.toString(currentFile)));
        TreeSet<String> strings = new TreeSet<>(comp);
        while ((str = in.readLine()) != null) {
            if (strings.size() == BUFFER_SIZE - 1) {
                for (String string : strings) {
                    out.write(string);
                    out.newLine();
                }
                out.write(str);
                out.close();

                files.add(make_name(folder, currentFile++));

                out = Files.newBufferedWriter(make_name(folder, currentFile));
                strings.clear();
            } else {
                strings.add(str);
            }
        }
        if(strings.size()>0) {
            boolean first = true;
            for (String string : strings) {
                if (!first) {
                    out.write('\n');
                }
                if(first) first=false;
                out.write(string);
            }
            out.close();
            files.add(make_name(folder, currentFile));
            strings.clear();
        }
        return files;
    }

    public static void main(String[] args) {
        if (args == null || Arrays.stream(args).anyMatch(Objects::isNull)) {
            System.err.println("All must not be nulls");
            return;
        }
        ArrayList<String> options = new ArrayList<>(), files = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintStream err = System.err, out = System.out;
        Arrays.stream(args).forEach(s -> {
            if (s.startsWith("-")) options.add(s);
            else files.add(s);
        });
        for (String s : files) {
            String[] elements = s.split("=");
            try {
                switch (elements[0]) {
                    case "out" -> out = new PrintStream(new FileOutputStream(Path.of(elements[1]).toFile()));
                    case "in" -> reader = Files.newBufferedReader(Path.of(elements[1]));
                    case "err" -> err = new PrintStream(new FileOutputStream(Path.of(elements[1]).toFile()));
                }
            } catch (IOException e) {
                System.err.println("Problems : \n" + e.getMessage());
            }
        }
        new Sort(takeComparator(options), reader, out, err);
    }
}

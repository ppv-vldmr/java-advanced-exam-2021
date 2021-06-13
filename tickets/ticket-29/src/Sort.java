import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public class Sort {
    private static final int BUFFER_SIZE = 1024;

    Path make_name(Path folder, int num) {
        return folder.resolve(Integer.toString(num));
    }

    void merge(BufferedReader[] ins, PrintStream out, Comparator<? super String> comp) throws IOException {
        Comparator<String> nullComparator = Comparator.comparing(Objects::isNull);
        nullComparator.thenComparing(comp);
        String[] strs = {ins[0].readLine(), ins[1].readLine()};
        Consumer<Integer> next = (Integer ind) -> {
            out.println(strs[ind]);
            try {
                strs[ind] = ins[ind].readLine();
            } catch (IOException e) {
                strs[ind] = null;
            }
        };
        while (Arrays.stream(strs).allMatch(Objects::isNull)) {
            switch (Integer.signum(nullComparator.compare(strs[0], strs[1]))) {
                case -1 -> next.accept(0);
                case 0 -> {
                    next.accept(0);
                    next.accept(1);
                }
                case 1 -> next.accept(1);
            }
        }
    }

    Sort(Comparator<? super String> comp, BufferedReader in, PrintStream out, PrintStream err) {
        Path folder = Path.of(System.getProperty("user.dir")).resolve(".sorting");
        ArrayDeque<Path> files;
        try {
            Files.createDirectories(folder);
            files = cut(in, folder, comp);
            int current
            for (int i = 0; 2 * i + 1 < files.size(); i++) {
                files.add(make_name(folder, files.size()));
                PrintStream tempOut = new PrintStream(new FileOutputStream(files.get(files.size() - 1).toFile()));
                merge(new BufferedReader[]{
                                Files.newBufferedReader(files.get(2 * i)),
                                Files.newBufferedReader(files.get(2 * i + 1))},
                        tempOut, comp);
            }

            while (files.size() > 1) {

            }
            BufferedReader outReader = Files.newBufferedReader(files.get(files.size()-1));
            String str;
            while((str = outReader.readLine())!=null) {
                out.println(str);
            }
        } catch (IOException e) {
            err.println(e.getMessage());
        }
    }

    ArrayDeque<Path> cut(BufferedReader in, Path folder, Comparator<? super String> comp) throws IOException {
        String str;
        ArrayDeque<Path> files = new ArrayDeque<>();
        int currentFile = 1, stringsInFile = 0;
        BufferedWriter out = Files.newBufferedWriter(folder.resolve(Integer.toString(currentFile)));
        TreeSet<String> strings = new TreeSet<>(comp);
        while ((str = in.readLine()) != null) {
            if (stringsInFile == BUFFER_SIZE - 1) {
                for (String string : strings) {
                    out.write(string);
                    out.newLine();
                }
                out.write(str);
                out.close();

                files.add(make_name(folder, currentFile++));
                currentFile++;
                stringsInFile = 0;

                out = Files.newBufferedWriter(make_name(folder, currentFile));
                strings.clear();
            } else {
                strings.add(str);
                stringsInFile++;
            }
        }
        return files;
    }

    static Comparator<? super String> takeComparator(String[] args) {

        return Comparator.naturalOrder();
    }

    public static void main(String[] args) {
        if(Arrays.stream(args).anyMatch(Objects::isNull)) {
            System.err.println("All must not be nulls");
        }
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        PrintStream out=System.out,err=System.err;
        int expectedLength = 1;
        try{
            if (args.length >= expectedLength && !args[0].matches("/-")) {
                in = Files.newBufferedReader(Path.of(args[0]));
                expectedLength++;
            }
            if (args.length >= expectedLength && !args[1].matches("/-")) {
                out = new PrintStream(new FileOutputStream(args[1]));
                expectedLength++;
            }
            if (args.length >= expectedLength && !args[2].matches("/-")) {
                out = new PrintStream(new FileOutputStream(args[1]));
            }
            new Sort(takeComparator(args),in,out,err);
        } catch (IOException e) {
            System.err.println("Problems with output/error output/input : " + e.getMessage());
        }
    }
}

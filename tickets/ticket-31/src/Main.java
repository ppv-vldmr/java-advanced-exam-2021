import java.util.ArrayList;
import java.util.List;

class Main {

    private static void checkArgs(String... args) {
        if (args == null) {
            System.err.println("Args shouldn't be null");
            return;
        }
        for (String arg : args) {
            if (arg == null) {
                System.err.println("Argument shouldn't be null");
                return;
            }
        }
    }

    public static void main(final String... args) {
        try {
            checkArgs(args);
            final int n = Integer.parseInt(args[0]);
            if (args.length != n + 2 && args.length != n + 1) {
                System.err.println("Format args:\n n [thread] <n numbers>");
                return;
            }
            int threads = 1;
            int start = 1;
            if (args.length == n + 2) {
                threads = Integer.parseInt(args[1]);
                start++;
            }
            final List<Integer> list = new ArrayList<>();
            for (int i = start; i <= n + start - 1; i++) {
                list.add( Integer.parseInt(args[i]));
            }
            printArray("Input array", list);
            ParallelSortUtils.parallelQuickSort(threads, list);
            printArray("Output array", list);
        } catch (final NumberFormatException e) {
            System.err.println("Only numbers");
        }
    }

    private static void printArray(String s, List<Integer> list) {
        System.out.print(s + ": ");
        for (Integer i : list) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
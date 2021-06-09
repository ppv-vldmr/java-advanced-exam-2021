import java.util.ArrayList;
import java.util.List;

class Main {



    public static void main(final String... args) {
        try {
            final int n = Integer.parseInt(args[0]);
            final int threads = Integer.parseInt(args[1]);
            final List<Integer> list = new ArrayList<>();
            for (int i = 2; i <= n + 1; i++) {
                list.add(i - 2, Integer.parseInt(args[i]));
            }
            ParallelSortUtils.parallelQuickSort(threads, list);
            for (final Integer i : list) {
                System.out.print(i + " ");
            }
            System.out.println();
        } catch (final NumberFormatException e) {
            System.out.println("Only numbers");
        }
    }
}
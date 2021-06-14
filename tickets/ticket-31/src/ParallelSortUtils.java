import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;


public class ParallelSortUtils {

    public static <E> void parallelQuickSort(final int threads, final List<E> sortedList) {
        parallelQuickSort(threads, sortedList, null);
    }

    public static <E> void parallelQuickSort(final int threads, final List<E> sortedList, final Comparator<? super E> comparator) {
        new ForkJoinPool(threads).invoke(new QuickSortTask<E>(sortedList, comparator));
    }


}

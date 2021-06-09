import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ParallelSortUtils {


    public static <E> void parallelQuickSort(final int threads, final List<E> sortedList) {
        parallelQuickSort(threads, null, sortedList);
    }

    public static <E> void parallelQuickSort(final int threads, final Comparator<? super E> comparator, final List<E> sortedList) {
        final ExecutorService executorService = Executors.newFixedThreadPool(threads);
        parallelQuickSort(executorService, sortedList, comparator, 0, sortedList.size() - 1);
        executorService.shutdown();
    }

    private static <E> E getMedian(final List<E> list, final Comparator<? super E> comparator, final int l, final int r) {
        final int mid = (l + r) / 2;
        final List<E> tmp_list = new ArrayList<>(List.of(list.get(l), list.get(r), list.get(mid)));
        for (int i = 0; i < 2; i++) {
            if (!compareElements(tmp_list.get(0), tmp_list.get(1), comparator)) {
                Collections.swap(tmp_list, 0, 1);
            }
            if (!compareElements(tmp_list.get(1), tmp_list.get(2), comparator)) {
                Collections.swap(tmp_list, 1, 2);
            }
        }
        return tmp_list.get(1);
    }

    private static <E> boolean compareElements(final E a, final E b, final Comparator<? super E> comparator) {
        if (comparator == null) {
            return ((Comparable<? super E>) a).compareTo(b) < 0;
        }
        return comparator.compare(a, b) < 0;
    }

    private static <E> int partition(final List<E> list,
                                     final Comparator<? super E> comparator,
                                     final int l, final int r) {
        final E v = getMedian(list, comparator, l, r);
        int i = l;
        int j = r;
        while (i <= j) {
            while (compareElements(list.get(i), v, comparator)) {
                i++;
            }
            while (!compareElements(list.get(j), v, comparator)) {
                j--;
            }
            if (i >= j) {
                break;
            }
            Collections.swap(list, i, j);
        }
        return j;
    }

    private static <E> void parallelQuickSort(final ExecutorService executorService,
                                              final List<E> list,
                                              final Comparator<? super E> comparator,
                                              final int l, final int r) {
        if (l < r) {
            final int mid = partition(list, comparator, l, r);
            if (mid < (l + r) / 2) {
                executorService.execute(() -> parallelQuickSort(executorService, list, comparator, l, mid));
                parallelQuickSort(executorService, list, comparator, mid + 1, r);
            } else {
                parallelQuickSort(executorService, list, comparator, l, mid);
                executorService.execute(() -> parallelQuickSort(executorService, list, comparator, mid + 1, r));
            }
        }

    }


}

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class ParallelSortUtils {


    private static void shutdownAndAwaitTermination(final ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (final InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }


    public static <E> void parallelQuickSort(final int threads, final List<E> sortedList) {
        parallelQuickSort(threads, null, sortedList);
    }

    public static <E> void parallelQuickSort(final int threads, final Comparator<? super E> comparator, final List<E> sortedList) {
        final ExecutorService executorService = Executors.newFixedThreadPool(threads);
        parallelQuickSort(executorService, sortedList, comparator, 0, sortedList.size() - 1);
        shutdownAndAwaitTermination(executorService);
    }

    private static <E> E getMedian(final List<E> list, final Comparator<? super E> comparator, final int l, final int r) {
        final int mid = (l + r) / 2;
        final List<E> tmp_list = new ArrayList<>(List.of(list.get(l), list.get(r), list.get(mid)));
        for (int i = 0; i < 2; i++) {
            if (compareElements(tmp_list.get(0), tmp_list.get(1), comparator) > 0) {
                Collections.swap(tmp_list, 0, 1);
            }
            if (compareElements(tmp_list.get(1), tmp_list.get(2), comparator) > 0) {
                Collections.swap(tmp_list, 1, 2);
            }
        }
        return tmp_list.get(1);
    }

    private static <E> int compareElements(final E a, final E b, final Comparator<? super E> comparator) {
        if (comparator == null) {
            return ((Comparable<? super E>) a).compareTo(b);
        }
        return comparator.compare(a, b);
    }

    private static <E> int partition(final List<E> list,
                                     final Comparator<? super E> comparator,
                                     final int l, final int r) {
        final E v = getMedian(list, comparator, l, r);
        int i = l;
        int j = r;
        while (i <= j) {
            while (compareElements(list.get(i), v, comparator) < 0) {
                i++;
            }
            while (compareElements(list.get(j), v, comparator) > 0) {
                j--;
            }
            if (i >= j) {
                break;
            }
            Collections.swap(list, i, j);
            i++;
            j--;
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

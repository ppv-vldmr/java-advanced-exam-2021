import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

public class QuickSortTask<E> extends RecursiveAction {

    private final List<E> list;
    private final Comparator<? super E> comparator;
    private final int l;
    private final int r;

    // Random threadsafe -> all right
    private final Random random;

    QuickSortTask(final List<E> list) {
        this(list, null);
    }

    QuickSortTask(final List<E> list, final Comparator<? super E> comparator) {
        this(list, comparator, new Random(), 0, list.size() - 1);
    }

    private QuickSortTask(final List<E> list, final Comparator<? super E> comparator, final Random random, final int l, final int r) {
        this.list = list;
        this.comparator = comparator;
        this.l = l;
        this.r = r;
        this.random = random;
    }

    @Override
    protected void compute() {
        if (l < r) {
            final int mid = partition();
            invokeAll(newTask(l, mid), newTask(mid + 1, r));
        }
    }

    private QuickSortTask<E> newTask(final int l, final int r) {
        return new QuickSortTask<>(list, comparator, random, l, r);
    }

    @SuppressWarnings("unchecked")
    private int compareElements(final E a, final E b) {
        if (comparator == null) {
            return ((Comparable<? super E>) a).compareTo(b);
        }
        return comparator.compare(a, b);
    }

    private E getMedian() {
        return list.get(random.nextInt(r - l) + l);
    }

    private int partition() {
        final E v = getMedian();
        int i = l;
        int j = r;
        while (i <= j) {
            while (compareElements(list.get(i), v) < 0) {
                i++;
            }
            while (compareElements(list.get(j), v) > 0) {
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
}

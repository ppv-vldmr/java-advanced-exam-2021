import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class QuickSortTask<E> extends RecursiveAction {

    private final List<E> list;
    private final Comparator<? super E> comparator;
    private final int l;
    private final int r;

    QuickSortTask(final List<E> list) {
        this(list, null);
    }

    QuickSortTask(final List<E> list, final Comparator<? super E> comparator) {
        this(list, comparator, 0, list.size() - 1);
    }

    private QuickSortTask(final List<E> list, final Comparator<? super E> comparator, final int l, final int r) {
        this.list = list;
        this.comparator = comparator;
        this.l = l;
        this.r = r;
    }


    @Override
    protected void compute() {
        if (l < r) {
            final int mid = partition();
            invokeAll(new QuickSortTask<>(list, comparator, l, mid), new QuickSortTask<>(list, comparator, mid + 1, r));
        }
    }

    @SuppressWarnings("unchecked")
    private int compareElements(final E a, final E b) {
        if (comparator == null) {
            return ((Comparable<? super E>) a).compareTo(b);
        }
        return comparator.compare(a, b);
    }

    private E getMedian() {
        final E a = list.get(l);
        final E b = list.get(r);
        final E c = list.get((l + r) / 2);
        if (compareElements(a, b) > 0) {
            if (compareElements(c, a) > 0) {
                return a;
            }
            if (compareElements(b, c) > 0) {
                return b;
            } else {
                return c;
            }
        }
        if (compareElements(c, b) > 0) {
            return b;
        }
        if (compareElements(a, c) > 0) {
            return a;
        }
        return c;
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

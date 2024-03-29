import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Tests {

    private <E> void check_equals(final List<E> list, final List<E> sortList) {
        for (int i = 0; i < list.size(); i++) {
            Assert.assertEquals(list.get(i), sortList.get(i));
        }
    }

    private <E> void base_test(final List<E> list, final List<E> sortList) {
        ParallelSortUtils.parallelQuickSort(4, list);
        check_equals(list, sortList);
    }

    private <E> void base_test(final List<E> list, final List<E> sortList, final Comparator<? super E> comparator) {
        ParallelSortUtils.parallelQuickSort(4, list, comparator);
        check_equals(list, sortList);
    }


    @Test
    public void test_01_just_number_list() {
        final List<Integer> list =     new ArrayList<>(List.of(2, 2, 8, 2, 2, 8, 1, 2, 3, 2, 1));
        final List<Integer> sortList = new ArrayList<>(List.of(1, 1, 2, 2, 2, 2, 2, 2, 3, 8, 8));
        base_test(list, sortList);
    }

    @Test
    public void test_02_comparator() {
        final List<Integer> list =     new ArrayList<>(List.of(2, 2, 8, 2, 2, 8, 1, 2, 3, 2, 1));
        final List<Integer> sortList = new ArrayList<>(List.of(8, 8, 3, 2, 2, 2, 2, 2, 2, 1, 1));
        base_test(list, sortList, Comparator.reverseOrder());
    }

    @Test
    public void test_03_string() {
        final List<String> list =     new ArrayList<>(List.of("b", "aa", "a", "ab", "z", "az", "aaz"));
        final List<String> sortList = new ArrayList<>(List.of("a", "aa", "aaz", "ab", "az", "b", "z"));
        base_test(list, sortList);
    }


    @Test
    public void test_04_string_comparator() {
        final List<String> list =     new ArrayList<>(List.of("b", "aa", "a", "ab", "z", "az", "aaz"));
        final List<String> sortList = new ArrayList<>(List.of("b", "z", "a", "ab", "az", "aa", "aaz"));
        final Comparator<String> comparator = Comparator.<String>comparingInt(string -> {
            int ans = 0;
            for (int i = 0; i < string.length(); i++) {
                if (string.charAt(i) == 'a') {
                    ans++;
                }
            }
            return ans;
        }).thenComparing(Comparator.naturalOrder());
        base_test(list, sortList, comparator);
    }

    @SuppressWarnings("unchecked")
    private void big_test(final int N, final int threads) {
        final ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i <= N; i++) {
            list.add(i);
        }
        final List<Integer> sortList = (List<Integer>) list.clone();
        Collections.shuffle(list);

        ParallelSortUtils.parallelQuickSort(threads, list);
        base_test(list, sortList);
    }

    @Test
    public void test_05_big_test() {
        big_test(31, 4);
    }

    @Test
    public void test_06_very_big_test() {
        big_test(1000_000, 10);
    }

    @Test
    public void test_07_very_big_test_one_thread() {
        big_test(1000_000, 1);
    }
}

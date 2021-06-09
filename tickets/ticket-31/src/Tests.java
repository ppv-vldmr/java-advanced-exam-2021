import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Tests {

    @Test
    public void test_01_just_number_list() {
        final List<Integer> list =     new ArrayList<>(List.of(2, 2, 8, 2, 2, 8, 1, 2, 3, 2, 1));
        final List<Integer> sortList = new ArrayList<>(List.of(1, 1, 2, 2, 2, 2, 2, 2, 3, 8, 8));
        ParallelSortUtils.parallelQuickSort(4, list);
        for (int i = 0; i < list.size(); i++) {
            Assert.assertEquals(list.get(i), sortList.get(i));
        }
    }

}

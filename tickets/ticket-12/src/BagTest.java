import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BagTest {

    @Test
    void addSingle() {
        Bag<Integer> bag = new Bag<>();
        bag.add(1);
        Assertions.assertTrue(bag.contains(1) && bag.size() == 1);
    }

    @Test
    void addDouble() {
        Bag<Integer> bag = new Bag<>();
        bag.add(1);
        bag.add(1);
        Assertions.assertTrue(bag.contains(1) && bag.size() == 2);
    }

    @Test
    void addAll() {
        Bag<Integer> bag = new Bag<>();
        bag.addAll(List.of(1, 1, 2, 2, 3));
        System.out.println(Arrays.toString(bag.toArray()));
        Assertions.assertTrue(bag.contains(1));
        Assertions.assertTrue(bag.contains(2));
        Assertions.assertTrue(bag.contains(3));
        assertEquals(5, bag.size());
    }

    @Test
    void clear() {
        Bag<Integer> bag = new Bag<>();
        bag.addAll(List.of(1, 1, 2, 2, 3));
        bag.clear();
        Assertions.assertTrue(bag.isEmpty());
    }

    @Test
    void contains() {
        Bag<Integer> bag = new Bag<>();
        bag.addAll(List.of(1, 1));
        Assertions.assertTrue(bag.contains(1));
    }

    @Test
    void containsAll() {
        Bag<Integer> bag = new Bag<>();
        bag.addAll(List.of(1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5));
        Assertions.assertTrue(bag.containsAll(List.of(1, 2, 3, 4, 5, 6)));
    }

    @Test
    void iterator() {
        Bag<Integer> numbers = new Bag<>();
        numbers.addAll(List.of(2, 3, 4, 3));
        for (Iterator<Integer> it = numbers.iterator(); it.hasNext();) {
            System.out.println(it.next());
            it.remove();
        }
        Assertions.assertDoesNotThrow(numbers.iterator()::hasNext);
    }

    @Test
    void remove() {
        Bag<Integer> numbers = new Bag<>();
        numbers.addAll(List.of(1, 1, 1, 1));
        Runnable r = () -> { System.out.println("SAHKA");};
        Bag<Runnable> runnableBag = new Bag<>();
        runnableBag.add(r);
        Assertions.assertTrue(
                runnableBag.remove(r)
                && numbers.remove(1)
                && !numbers.remove(2)
                && !runnableBag.remove(r));
    }

    @Test
    void retainAll() {
    }

    @Test
    void size() {
        final int sz = 10;
        Bag<Integer> numbers = new Bag<>();
        for (int i = 0; i < sz; i++) {
            numbers.add(i);
        }
        Assertions.assertEquals(numbers.size(), sz);
    }

    @Test
    void toArray() {
    }

    @Test
    void testToArray() {
    }
}
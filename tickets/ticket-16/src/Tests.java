import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Tests {
    @Test
    public void test_01_createLazyList() {
        LazyList<Integer> lazyList = new LazyList<>(List.of(
                () -> 0,
                () -> 1
        ));
        Assert.assertEquals(Integer.valueOf(1), lazyList.get(1));
        Assert.assertEquals(Integer.valueOf(0), lazyList.get(0));
        lazyList.shutdown();
    }

    private <T> Callable<T> getCallableWithSleep(T defaultValue) {
        return () -> {
            Thread.sleep(1000);
            return defaultValue;
        };
    }

    private long calculateTime(Supplier<Integer> operation) {
        long currentTime = System.currentTimeMillis();
        int i = operation.get();
        long c = System.currentTimeMillis() - currentTime;
        System.out.println("Test: " + i + " Time: " + c);
        return c;
    }

    @Test
    public void test_02_calculateSeveralTimes() {
        final int currentDefaultValue = 0;
        LazyList<Integer> lazyList = new LazyList<>(
                List.of(
                        getCallableWithSleep(currentDefaultValue)
                )
        );
        Assert.assertTrue(calculateTime(() -> {
            int i1 = lazyList.get(0);
            int i2 = lazyList.get(0);
            int i3 = lazyList.get(0);
            int i4 = lazyList.get(0);
            Assert.assertEquals(i1, currentDefaultValue);
            Assert.assertEquals(i2, currentDefaultValue);
            Assert.assertEquals(i3, currentDefaultValue);
            Assert.assertEquals(i4, currentDefaultValue);
            return 2;
        }) <= 1100);
        lazyList.shutdown();
    }

    private <T> List<Callable<T>> getListCallables(int size, T defaultValue) {
        List<Callable<T>> answer = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            answer.add(getCallableWithSleep(defaultValue));
        }
        return answer;
    }

    private <T> Thread getThread(int index, LazyList<T> lazyList, T defaultValue) {
        return new Thread(() -> {
            T i1 = lazyList.get(index);
            T i2 = lazyList.get(index);
            T i3 = lazyList.get(index);
            Assert.assertEquals(i1, defaultValue);
            Assert.assertEquals(i2, defaultValue);
            Assert.assertEquals(i3, defaultValue);
        });
    }

    private <T> List<Thread> getThreadList(int size, LazyList<T> lazyList, T defaultValue) {
        List<Thread> answer = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            answer.add(getThread(i, lazyList, defaultValue));
        }
        return answer;
    }

    @Test
    public void test_03_calculateInSeveralThreads() {
        baseCalculateInSeveralThreads(5, 0, 3);
    }

    @Test
    public void test_04_calculateInSeveralThreadsBig() {
        baseCalculateInSeveralThreads(100, 0, 4);
    }


    private void baseCalculateInSeveralThreads(int currentSize, int currentDefaultValue, int test) {
        LazyList<Integer> lazyList = new LazyList<>(getListCallables(currentSize, currentDefaultValue));
        List<Thread> threadList =  getThreadList(currentSize, lazyList, currentDefaultValue);
        final long expectedTime = getExpectedTime(currentSize);
        Assert.assertTrue(calculateTime(
                () -> {
                    for (Thread thread : threadList) {
                        thread.start();
                    }
                    for (Thread thread : threadList) {
                        try {
                            thread.join();
                        } catch (InterruptedException e) {
                            // No operations
                        }
                    }
                    return test;
                }
        ) <= expectedTime);
        lazyList.shutdown();
    }

    private long getExpectedTime(int currentSize) {
        final int cores = Runtime.getRuntime().availableProcessors();
        return 1000L * ((currentSize + cores - 1) / cores) + 250;
    }

    private List<Callable<Integer>> getIotaCallable(int size) {
        List<Callable<Integer>> answer = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            answer.add(getCallableWithSleep(i));
        }
        return answer;
    }

    @Test
    public void test_05_contains() {
        final int currentSize = 5;
        List<Callable<Integer>> iotaList = getIotaCallable(currentSize);
        Collections.shuffle(iotaList);
        LazyList<Integer> lazyList = new LazyList<>(iotaList);
        long expectedTime = getExpectedTime(currentSize);
        long wasTime = calculateTime(() -> {
            boolean three = lazyList.contains(3);
            Assert.assertTrue(three);
            Assert.assertTrue(lazyList.contains(5));
            Assert.assertFalse(lazyList.contains(6));
            return 5;
        });
        Assert.assertTrue(wasTime <= expectedTime);
    }

    @Test
    public void test_allSeveralTimes() {
        for (int i = 0; i < 5; i++) {
            test_01_createLazyList();
            test_02_calculateSeveralTimes();
            test_03_calculateInSeveralThreads();
            test_04_calculateInSeveralThreadsBig();
            test_05_contains();
        }
    }
}

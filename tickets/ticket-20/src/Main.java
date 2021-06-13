import java.util.List;
import java.util.Random;

public class Main {

    private static final int NUMBER_OF_ELEMENTS = 10;
    private static final int NUMBER_OF_ITERATION = 20;
    private static final int MAX_SLEEP_FIRST_THREAD = 800;
    private static final int SLEEP_SECOND_THREAD = 700;

    private static Thread getFirstThread(MyConcurrentQueue<Integer> queue) {
        return new Thread(() -> {
            Random random = new Random();
            for (int i = 0; i < NUMBER_OF_ITERATION; i++) {
                try {
                    Thread.sleep(Math.abs(random.nextInt()) % MAX_SLEEP_FIRST_THREAD);
                    queue.add(i);
                    printMessage("First thread add to queue element " + i + ". Current elements : " + queue.size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private static Thread getSecondThread(MyConcurrentQueue<Integer> queue) {
        return new Thread(() -> {
            for (int i = 0; i < NUMBER_OF_ITERATION; i++) {
                try {
                    Thread.sleep(SLEEP_SECOND_THREAD);
                    printMessage("Second thread removed an element from queue: " + queue.remove() + ". Current elements: " + queue.size());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
    }

    private static void printMessage(String message) {
        synchronized (System.out) {
            System.out.println(message);
        }
    }

    private static void startThreads(List<Thread> threadList) {
        for (Thread thread : threadList) {
            thread.start();
        }
    }

    private static void joinThreads(List<Thread> threadList) throws InterruptedException {
        for (int i = 0; i < threadList.size(); i++) {
            try {
                threadList.get(i).join();
            } catch (InterruptedException e) {
                e.addSuppressed(interruptedThreads(i,"Cannot join threads" , threadList));
                throw e;
            }
        }
    }

    private static InterruptedException interruptedThreads(final int begin, String message, final List<Thread> threadList) {
        final InterruptedException interruptedException = new InterruptedException(message);
        for (int i = begin; i < threadList.size(); i++) {
            threadList.get(i).interrupt();
        }
        for (int i = begin; i < threadList.size(); i++) {
            try {
                threadList.get(i).join();
            } catch (final InterruptedException e) {
                interruptedException.addSuppressed(e);
                i--;
            }
        }
        return interruptedException;
    }

    public static void main(String... args) throws InterruptedException {
        MyConcurrentQueue<Integer> queue = new MyConcurrentQueue<>(NUMBER_OF_ELEMENTS);
        List<Thread> threadList = List.of(getFirstThread(queue), getSecondThread(queue));
        startThreads(threadList);
        joinThreads(threadList);
    }
}
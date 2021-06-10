import java.util.Random;

public class Main {

    private static final int NUMBER_OF_ELEMENTS = 10;
    private static final int NUMBER_OF_ITERATION = 20;
    private static final int MAX_ANDREY_SLEEP = 1200;
    private static final int PART_TIME_SLEEP_GEORGIY = 1000;

    public static void main(String... args) {
        MyConcurrentQueue<Integer> queue = new MyConcurrentQueue<>(NUMBER_OF_ELEMENTS);

        Thread andrey = new Thread(() -> {
            Random random = new Random();
            for (int i = 0; i < NUMBER_OF_ITERATION; i++) {
                try {
                    Thread.sleep(Math.abs(random.nextInt()) % MAX_ANDREY_SLEEP);
                    queue.add(i);
                    System.out.println("Andrey add to queue element " + i + ". Current elements : " + queue.size());
                } catch (InterruptedException e) {
                    // TODO: шо делать хз
                    // No operations
                }
            }
        });
        Thread georgiy = new Thread(() -> {
            for (int i = 0; i < NUMBER_OF_ITERATION; i++) {
                try {
                    Thread.sleep(PART_TIME_SLEEP_GEORGIY);
                    System.out.println("Georgiy removed an element from queue: " + queue.remove() + ". Current elements: " + queue.size());
                } catch (InterruptedException e) {
                    // TODO: шо делать хз
                    // No operations
                }
            }
        });
        georgiy.start();
        andrey.start();
    }
}
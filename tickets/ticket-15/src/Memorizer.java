import java.util.Queue;
import java.util.concurrent.*;
import java.util.function.Function;

@SuppressWarnings({"unused"})
public class Memorizer<T, R> implements AutoCloseable {
    private final Function<T, R> function;
    private final ConcurrentMap<T, Future<R>> cacheMap;
    private final int
            THREADS_AMOUNT = 4,
            QUEUE_SIZE = 64;
    private final ExecutorService threadPool;
    private final Queue<T> tasksQueue, computingQueue;

    public Memorizer(final Function<T, R> function) {
        this.function = function;
        cacheMap = new ConcurrentHashMap<>();
        threadPool = Executors.newFixedThreadPool(THREADS_AMOUNT);
        tasksQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        computingQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
    }

    // нужно сделать очередь с заданиями

    public String apply(final T arg) throws ExecutionException, InterruptedException {
        final Future<R> cacheVar = cacheMap.get(arg);

        if (cacheVar != null) {
            return cacheVar.get().toString() + " cached";
        }

        if (!tasksQueue.contains(arg)
                && !computingQueue.contains(arg)) {     // а если прямо сейчас выполняется?
            tasksQueue.add(arg);                 // видимо еще нужна очередь computing
            final Future<R> val = threadPool.submit(this::solveTask);
            if (val.get() == null) {
                return cacheMap.get(arg).get().toString() + " cached";
            }
            cacheMap.put(arg, val);
            return val.get().toString();
        } else {
            return cacheMap.get(arg).get().toString();
        }
    }

    private R solveTask() {
//        if (tasksQueue.isEmpty()) {
//            return null;
//        }
        try {
            Thread.currentThread().wait(1000);
        } catch (InterruptedException e) {
            // no operations
        }
        final T arg = tasksQueue.poll();
        computingQueue.add(arg);
        final R result = function.apply(arg);
        computingQueue.poll();
        return result;
    }

    private void printAnswer(final String result) {
        // решить проблему со множественным выводом одного и того же - очередь?
        synchronized (System.out) {
            System.out.println(result);
        }
    }


    @Override
    public void close() {
        shutdownAndAwaitTermination(threadPool);
        cacheMap.clear();
        tasksQueue.clear();
    }

    public void shutdownAndAwaitTermination(final ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(10, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (final InterruptedException ie) {
            pool.shutdownNow();
        }
    }
}

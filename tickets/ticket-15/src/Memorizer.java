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

    public Memorizer(Function<T, R> function) {
        this.function = function;
        cacheMap = new ConcurrentHashMap<>();
        threadPool = Executors.newFixedThreadPool(THREADS_AMOUNT);
        tasksQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        computingQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
    }

    // нужно сделать очередь с заданиями

    public String apply(T arg) throws ExecutionException, InterruptedException {
        Future<R> cacheVar = cacheMap.get(arg);

        if (cacheVar != null) {
            return cacheVar.get().toString() + " cached";
        }

        if (!tasksQueue.contains(arg)
                && !computingQueue.contains(arg)) {     // а если прямо сейчас выполняется?
            tasksQueue.add(arg);                 // видимо еще нужна очередь computing
            Future<R> val = threadPool.submit(this::solveTask);
            cacheMap.put(arg, val);
            return val.get().toString();
        } else {
            return cacheMap.get(arg).get().toString();
        }
    }

    private R solveTask() {
        if (tasksQueue.isEmpty()) {
            return null;
        }
        T arg = tasksQueue.poll();
        computingQueue.add(arg);
        R result = function.apply(arg);
        computingQueue.poll();
        return result;
    }

    private void printAnswer(String result) {
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

    public void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(10, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
        }
    }
}

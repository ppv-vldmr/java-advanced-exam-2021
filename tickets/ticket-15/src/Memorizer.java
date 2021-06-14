import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.*;
import java.util.function.Function;

@SuppressWarnings({"unused"})
public class Memorizer<T extends Comparable<T>, R> implements AutoCloseable, Function<T, R> {
    private final Function<T, R> function;
    private final ConcurrentMap<T, Future<R>> cacheMap;
    private final int THREADS_AMOUNT = 4, QUEUE_SIZE = 64;
    private final ExecutorService threadPool;
    private final Queue<T> tasksQueue;
    private final Set<T> computingSet;

    public Memorizer(final Function<T, R> function) {
        this.function = function;
        cacheMap = new ConcurrentHashMap<>();
        threadPool = Executors.newFixedThreadPool(THREADS_AMOUNT);
        tasksQueue = new ArrayBlockingQueue<>(QUEUE_SIZE);
        computingSet = new TreeSet<>();
    }

    @Override
    public R apply(final T arg) {
        final Future<R> cacheVar = cacheMap.get(arg);

        if (cacheVar != null) {
            try {
                return cacheVar.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        }

        if (!tasksQueue.contains(arg) && !computingSet.contains(arg)) {
            tasksQueue.add(arg);
            final Future<R> val = threadPool.submit(this::solveTask);
            cacheMap.put(arg, val);
            try {
                return val.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            try {
                return cacheMap.get(arg).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private R solveTask() throws ExecutionException, InterruptedException {
        T arg = tasksQueue.poll();

        synchronized (computingSet) {
            if (computingSet.contains(arg)) {
                return cacheMap.get(arg).get();
            } else {
                computingSet.add(arg);
            }
        }

        final R result = function.apply(arg);
        computingSet.remove(arg);
        return result;
    }

    @Override
    public void close() {
        shutdownAndAwaitTermination(threadPool);
        cacheMap.clear();
        tasksQueue.clear();
        computingSet.clear();
    }

    public void shutdownAndAwaitTermination(final ExecutorService pool) {   // private
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

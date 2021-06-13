import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LazyList<T> extends AbstractList<T> implements ElementCalculator<T>, List<T> {


    List<FutureTask<T>> futureTasks;
    ExecutorService executorService;

    public LazyList(final int size) {
        this(new ArrayList<>(Collections.nCopies(size, new FutureTask<>(() -> null))));
    }

    public LazyList(final List<Callable<T>> tasksList) {
        this(tasksList.stream()
                .map(LazyList::createFutureTask)
                .collect(Collectors.toCollection(ArrayList::new)));
    }


    private LazyList(final ArrayList<FutureTask<T>> futureTasks) {
        executorService = Executors.newCachedThreadPool();
        this.futureTasks = futureTasks;
    }

    //TODO: нужен ли synchronized (нужен)
    private static <E> FutureTask<E> createFutureTask(final Callable<E> callable) {
        return new FutureTask<>(() -> {
            synchronized (callable) {
                return callable.call();
            }
        });
    }


    // TODO: потокобезопасная ли эта операция?
    @Override
    public T calculate(final int index) throws ExecutionException, InterruptedException {

        if (executorService.isShutdown()) {
            throw new InterruptedException();
        }
        if (!futureTasks.get(index).isDone()) {
            synchronized (futureTasks.get(index)) {
                if (!futureTasks.get(index).isDone()) {
                    executorService.execute(futureTasks.get(index));
                }
            }
        }
        return futureTasks.get(index).get();
    }

    @Override
    public int size() {
        return futureTasks.size();
    }

    @Override
    public boolean isEmpty() {
        return futureTasks.isEmpty();
    }

    @Override
    public boolean contains(final Object o) {
        // TODO: потокобезопасная ли эта операция?
        return IntStream.range(0, size()).parallel().mapToObj(this::get).anyMatch(o::equals);
    }


    private void shutdownAndAwaitTermination() {
        executorService.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (final InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            executorService.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown() {
        shutdownAndAwaitTermination();
    }


    @Override
    public T get(final int i) {
        try {
            return calculate(i);
        } catch (final ExecutionException | InterruptedException e) {
            // todo: шо делать?
            System.err.println("test print in get in lazylist" + e.getMessage());
            return null;
        }
    }


    public T set(final int i, final Callable<T> callable) throws ExecutionException, InterruptedException {
        final T answer = calculate(i);
        synchronized (futureTasks.get(i)) {
            futureTasks.set(i, LazyList.createFutureTask(callable));
        }
        return answer;
    }

}
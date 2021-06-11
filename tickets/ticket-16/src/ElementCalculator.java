import java.util.concurrent.ExecutionException;

public interface ElementCalculator<T> {
    T calculate(int index) throws ExecutionException, InterruptedException;
}

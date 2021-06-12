import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Function<Integer, Integer> func = x -> x * x;


        try (Memorizer<Integer, Integer> memo = new Memorizer<>(func)) {
            System.out.println(memo.apply(5));
            System.out.println(memo.apply(5));
        }
    }
}

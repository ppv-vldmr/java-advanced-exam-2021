import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import solution.Memorizer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("First level")
public class TrueTest {
    private static int testNumber = 1;
    private final static Random random = new Random();
    private static final Map<Integer, AtomicInteger> enterMap = new ConcurrentHashMap<>();

    @BeforeEach
    public void printTestNumber() {
        System.err.println("\n\tChecking test " + testNumber);
        testNumber++;
    }

    private static List<Integer> intGenerator(int size) {
        return random.ints(-3, 2).limit(size).boxed().collect(Collectors.toList());
    }
// 3571

    // one Thread
    private Integer getTrueAnswer(Function<Integer, Integer> function,
                                 HashMap<Integer, Integer> map,
                                 Integer arg) {
        Integer value = map.get(arg);
        if (value != null) {
            return value;
        }
        return function.apply(arg);
    }

    @ParameterizedTest
    @MethodSource("argumentsStreamGenerator")
    public void mainTester(Function<Integer, Integer> f, List<Integer> args) {
        f = new Memorizer<>(f);
        System.out.println("testing on input: " + args.toString());

        for (Integer arg : args) {
            f.apply(arg);
        }

        for (Integer i : enterMap.keySet()) {
            assertEquals(1, enterMap.get(i).get());
        }

        enterMap.clear();
        ((Memorizer<Integer, Integer>) f).close();
    }

    private static Stream<Arguments> argumentsStreamGenerator() {
        Function<Integer, Integer> function = x -> {
            if (enterMap.containsKey(x)) {
                enterMap.get(x).incrementAndGet();
            } else {
                enterMap.put(x, new AtomicInteger(1));
            }
            return x * x;
        };

        List<Arguments> res = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            res.add(Arguments.of(function, intGenerator(i)));
        }
        return res.stream();
    }

    @AfterAll
    public static void finish() {
        System.out.println("================================= Test finished");
    }
}

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DisplayName("First level")
public class MyTest {
    private int testNumber = 1;
    private final String descriptionPrefix = "function: ";

    @BeforeEach
    public void printTestNumber() {
        System.err.println("\n\tChecking test " + testNumber);
        testNumber++;
    }

    private List<Integer> intGenerator(int size) {
        return new Random().ints(-3, 2).limit(size).boxed().collect(Collectors.toList());
    }
// 3571

    // one Thread
    private String getTrueAnswer(Function<Integer, Integer> function,
                                 HashMap<Integer, Integer> map,
                                 Integer arg) {
        Integer value = map.get(arg);
        if (value != null) {
            return value.toString() + " cached";
        }
        return function.apply(arg).toString();
    }

    @Test
    public void test01() {
        final String description = descriptionPrefix + "x -> x * x";
        Function<Integer, Integer> f = x -> x + x;


        for (int i = 1; i < 11; i++) {
            var memorizer = new Memorizer<>(f);
            var numberList = intGenerator(i);
            var map = new HashMap<Integer, Integer>();
            System.out.println("testing on input: " + numberList.toString());

            for (int j = 0; j < numberList.size(); j++) {
                try {
                    String memoAnswer = memorizer.apply(numberList.get(j));

                    System.out.println(getTrueAnswer(f, map, numberList.get(j)));
                    System.out.println(memoAnswer + "\n");

                    assertEquals(memoAnswer, getTrueAnswer(f, map, numberList.get(j)));

                    map.put(numberList.get(j), f.apply(numberList.get(j)));

                } catch (ExecutionException | InterruptedException e) {
                    System.err.println(e.getMessage());
                }
            }
            map.clear();
            memorizer.close();
        }
    }


    @AfterAll
    public static void finish() {
        System.out.println("================================= Test finished");
    }
}

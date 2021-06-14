import java.util.Collection;
import java.util.List;

public class Main {


    public static void main(final String... args) {
        final Collection<Integer> linkedBag = new LinkedBag<>(List.of(1, 2, 3, 4, 5, 1, 1, 2, 5, 1));

        System.out.println(linkedBag);
        System.out.println();
        for (final Integer i : List.of(2, 1, 7, 8, 3)) {
            linkedBag.remove(i);
            System.out.println("Remove element " + i + ".\n\tCurrent linkedBag: " + linkedBag);
        }
        System.out.println();
        final List<Integer> testList = List.of(1, 4, 3, 7, 5, 5);
        for (final Integer i : testList) {
            System.out.println("element " + i + " contains in linkedBag " + linkedBag + ": " + linkedBag.contains(i));
        }

        for (final List<Integer> list : List.of(testList,
                List.of(1, 4, 5, 5),
                List.of(5, 5, 5, 5))) {
            System.out.println("contains all elements " + list + " in linkedBag " + linkedBag + ": " + linkedBag.containsAll(list));
        }

        System.out.println("Empty linkedBag: " + linkedBag.isEmpty());
        linkedBag.clear();
        System.out.println("Empty linkedBag after clear: " + linkedBag.isEmpty());

    }
}

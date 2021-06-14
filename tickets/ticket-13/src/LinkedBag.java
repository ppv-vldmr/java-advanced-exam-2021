import java.util.*;

public class LinkedBag<E> extends AbstractCollection<E> {


    private final List<E> elements;
    private final Map<E, EqualsElements<E>> mapFromElementToEqualsElements;

    public LinkedBag() {
        elements = new LinkedList<>();
        mapFromElementToEqualsElements = new HashMap<>();
    }

    public LinkedBag(final Collection<E> collection) {
        this();
        addAll(collection);
    }


    public boolean add(final E e) {
        elements.add(e);
        mapFromElementToEqualsElements.putIfAbsent(e, new EqualsElements<>());
        return mapFromElementToEqualsElements.get(e).add(e);
    }

    public boolean remove(final Object e) {
        final EqualsElements<E> equalsElements = mapFromElementToEqualsElements.get(e);
        if (equalsElements == null) {
            return false;
        }
        final E removeElement = equalsElements.remove();
        if (removeElement != null) {
            elements.remove(removeElement);
            return true;
        }
        return false;
    }

    public boolean contains(final Object o) {
        final EqualsElements<E> equalsElements = mapFromElementToEqualsElements.get(o);
        if (equalsElements == null) {
            return false;
        }
        return !equalsElements.isEmpty();
    }

    public void clear() {
        super.clear();
        mapFromElementToEqualsElements.clear();
    }

    @Override
    public Iterator<E> iterator() {
        return elements.iterator();
    }

    @Override
    public int size() {
        return elements.size();
    }

    private static class EqualsElements<T> {
        Queue<T> queue;

        EqualsElements() {
            queue = new ArrayDeque<>();
        }

        boolean add(final T element) {
            if (queue.isEmpty() || queue.peek().equals(element)) {
                queue.add(element);
                return true;
            }
            return false;
        }

        T remove() {
            return queue.poll();
        }

        boolean isEmpty() {
            return queue.isEmpty();
        }
    }

}

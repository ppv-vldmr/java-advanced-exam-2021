import java.util.*;
import java.util.stream.Stream;

public class Bag<T> extends AbstractCollection<T> {

    private final Map<T, Union<T>> elements;

    public Bag() {
        elements = new HashMap<>();
    }



    public Bag(Collection<T> coll) {
        this();
        addAll(coll);
    }

    private static class Union<T> {
        private final Deque<T> elements = new ArrayDeque<>();

        Union() { }

        T delegate() {
            if (!elements.isEmpty()) {
                return elements.getFirst();
            } else {
                throw new IllegalArgumentException("elements are empty");
            }
        }

        void add(T el) {
            if (elements.isEmpty() || el.equals(delegate())) {
                elements.add(el);
            }
        }

        int size() {
            return elements.size();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Union<?>)) {
                return false;
            }
            return delegate().equals(
                ((Union<?>)obj).delegate());
        }

        Stream<T> stream() {
            return elements.stream();
        }

        Iterator<T> iterator() {
            return elements.iterator();
        }
    }

    @Override
    public boolean add(T e) {
        elements.putIfAbsent(e, new Union<T>());
        elements.get(e).add(e);
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> coll) {
        coll.forEach(this::add);
        return true;
    }

    @Override
    public void clear() {
        elements.clear();
    }

    @Override
    public boolean contains(Object o) {
        return elements.containsKey(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return elements.keySet().containsAll(c);
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    private static class BagIterator<T> implements Iterator<T> {
        private Iterator<Union<T>> it;
        private Iterator<T> unionIterator;
        private Union<T> currUnion;
        
        BagIterator(Bag<T> bag) {
            this.it = bag.elements.values().iterator();
            if (it.hasNext()) {
                currUnion = it.next();
                unionIterator = currUnion.iterator();
            }
        }

        @Override
        public boolean hasNext() {
            if (unionIterator == null) {
                return false;
            }
            return unionIterator.hasNext() || it.hasNext();
        }

        @Override
        public T next() {
            if (!unionIterator.hasNext() && it.hasNext()) {
                currUnion = it.next();
                unionIterator = currUnion.iterator();
            }
            return unionIterator.next();
        }
        
        @Override
        public void remove() {
            // currUnion.delegate()
            unionIterator.remove();
            if (currUnion.size() == 0) {
                it.remove();
            }
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new BagIterator<T>(this);
    }

    @Override
    public boolean remove(Object o) {
        Union<T> union = elements.remove(o);
        if (union == null) {
            return false;
        } else {
            if (union.elements.size() > 1) {
                T del = union.elements.removeLast();
                elements.put(del, union);
            }
            return true;
        }
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        int startSize = size();
        c.forEach(this::remove);
        return startSize - size() == c.size();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return elements.keySet().retainAll(c);
    }

    @Override
    public int size() {
        return elements.values().stream().mapToInt(Union::size).sum();
    }

    @Override
    public Object[] toArray() {
        return elements.values()
            .stream()
            .flatMap(Union::stream)
            .toArray();
    }

    @Override
    public <U> U[] toArray(U[] a) {
        Object[] ar = toArray();
        System.arraycopy(ar, 0, a, 0, Math.min(a.length, ar.length));
        return a;
    }
    
}
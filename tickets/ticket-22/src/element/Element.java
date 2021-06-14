package element;

public interface Element<T extends Number> {
    T get();

    Element<T> add(final Element<T> other);

    Element<T> multiply(final Element<T> other);

    void set(final T value);
}

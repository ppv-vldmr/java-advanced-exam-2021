package element;

public abstract class AbstractElement<T extends Number> implements Element<T> {
    protected T value;

    public AbstractElement(final T value) {
        this.value = value;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void set(final T value) {
        this.value = value;
    }
}

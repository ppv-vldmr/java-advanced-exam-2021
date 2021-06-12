package element;

public class LongElement extends AbstractElement<Long> {

    public LongElement(final long value) {
        super(value);
    }

    @Override
    public LongElement add(Element<Long> other) {
        return new LongElement(value + other.get());
    }

    @Override
    public LongElement multiply(Element<Long> other) {
        return new LongElement(value * other.get());
    }
}

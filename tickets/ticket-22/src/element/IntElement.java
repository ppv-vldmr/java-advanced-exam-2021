package element;

public class IntElement extends AbstractElement<Integer> {

    public IntElement(final int value) {
        super(value);
    }

    @Override
    public IntElement add(Element<Integer> other) {
        return new IntElement(value + other.get());
    }

    @Override
    public IntElement multiply(Element<Integer> other) {
        return new IntElement(value * other.get());
    }
}

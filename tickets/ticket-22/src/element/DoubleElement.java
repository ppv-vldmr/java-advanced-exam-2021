package element;

public class DoubleElement extends AbstractElement<Double> {

    public DoubleElement(final double value) {
        super(value);
    }

    @Override
    public DoubleElement add(Element<Double> other) {
        return new DoubleElement(value + other.get());
    }

    @Override
    public DoubleElement multiply(Element<Double> other) {
        return new DoubleElement(value * other.get());
    }
}

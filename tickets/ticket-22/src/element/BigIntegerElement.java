package element;

import java.math.BigInteger;

public class BigIntegerElement extends AbstractElement<BigInteger> {

    public BigIntegerElement(BigInteger value) {
        super(value);
    }

    @Override
    public BigIntegerElement add(Element<BigInteger> other) {
        return new BigIntegerElement(value.add(other.get()));
    }

    @Override
    public BigIntegerElement multiply(Element<BigInteger> other) {
        return new BigIntegerElement(value.multiply(other.get()));
    }
}

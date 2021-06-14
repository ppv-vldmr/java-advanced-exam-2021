package element;

import java.math.BigDecimal;

public class BigDecimalElement extends AbstractElement<BigDecimal> {

    public BigDecimalElement(BigDecimal value) {
        super(value);
    }

    @Override
    public BigDecimalElement add(Element<BigDecimal> other) {
        return new BigDecimalElement(value.add(other.get()));
    }

    @Override
    public Element<BigDecimal> multiply(Element<BigDecimal> other) {
        return new BigDecimalElement(value.multiply(other.get()));
    }
}

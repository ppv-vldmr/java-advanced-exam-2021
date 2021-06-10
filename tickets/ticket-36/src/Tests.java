import game.Position;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class Tests {
    @Test
    void positionToStringTests() {
        Assert.assertEquals("a1", new Position("a1"));
        Assert.assertEquals("g6", new Position("g6"));
        Assert.assertEquals("e8", new Position("e8"));
        Assert.assertEquals("h8", new Position("h8"));
    }
}

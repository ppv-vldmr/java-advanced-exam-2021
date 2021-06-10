import game.Position;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Tests {
    @Test
    void positionToStringTests() {
        Assertions.assertEquals("a1", new Position("a1").toString());
        Assertions.assertEquals("g6", new Position("g6").toString());
        Assertions.assertEquals("e8", new Position("e8").toString());
        Assertions.assertEquals("h8", new Position("h8").toString());
    }
}

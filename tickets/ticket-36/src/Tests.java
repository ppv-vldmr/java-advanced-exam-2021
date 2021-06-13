import game.Game;
import game.Move;
import game.Position;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class Tests {
    @Test
    void positionToStringTests() {
        Assertions.assertEquals("a1", new Position("a1").toString());
        Assertions.assertEquals("g6", new Position("g6").toString());
        Assertions.assertEquals("e8", new Position("e8").toString());
        Assertions.assertEquals("h8", new Position("h8").toString());
    }

    void randomTest(Position[] pos) {
        Game game = new Game(pos);

        Position blackKing = pos[2];
        while (!game.isFinished()) {
            System.out.println(game.draw());
            game.whiteMove();
            System.out.println(game.draw());

            if (game.isFinished()) {
                System.out.println("FINISHED:\n" + game.draw());
                return;
            }
            Random r = new Random();
            int x, y;
            Position p;
            do {
                x = r.nextInt(3) - 1;
                y = r.nextInt(3) - 1;
                while (x == 0 && y == 0) {
                    x = r.nextInt(2) - 1;
                    y = r.nextInt(2) - 1;
                }
                p = new Position(blackKing.x + x, blackKing.y + y);
            } while (!p.isCorrect() || Game.isAttackKings(p, pos[0]) ||
                    Game.isAttackRookAndKing(p, pos[1])) ;
            blackKing.x += x;
            blackKing.y += y;

            game.blackMove(blackKing);
        }
    }

    @Test
    void randomTest01() {
        Position[] pos = new Position[3];
        pos[0] = new Position("g3");
        pos[1] = new Position("h2");
        pos[2] = new Position("g1");

        randomTest(pos);
    }

    @Test
    void randomTest02() {
        Position[] pos = new Position[3];
        pos[0] = new Position("f3");
        pos[1] = new Position("h2");
        pos[2] = new Position("a1");

        randomTest(pos);
    }

    @Test
    void randomTest03() {
        Position[] pos = new Position[3];
        pos[0] = new Position("b3");
        pos[1] = new Position("h2");
        pos[2] = new Position("a8");

        randomTest(pos);
    }

    @Test
    void randomTest04() {
        Position[] pos = new Position[3];
        pos[0] = new Position("g5");
        pos[1] = new Position("h8");
        pos[2] = new Position("g3");

        randomTest(pos);
    }
}

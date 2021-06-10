package game;

import java.util.Objects;

public class Position {
    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(String pos) {
        if (pos.matches("[a-h][1-8]")) {
            x = pos.charAt(0) - 'a';
            y = pos.charAt(1) - '1';
        }
    }

    @Override
    public String toString() {
        return Character.toString('a' + (char) x) +
                Character.toString('1' + y);
    }

    public Position copy() {
        return new Position(x, y);
    }

    public boolean isCorrect() {
        return 0 <= x && x < 8 &&
                0 <= y && y < 8;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

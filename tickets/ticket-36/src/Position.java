public class Position {
    int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(String pos) {
        if (pos.matches("[1-8a-h]")) {
            x = pos.charAt(0) - '1';
            y = pos.charAt(1) - 'a';
        }
    }

    @Override
    public String toString() {
        return String.valueOf('1' + (char) x + 'a' + (char) y);
    }
}

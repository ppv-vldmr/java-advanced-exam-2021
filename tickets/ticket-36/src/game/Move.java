package game;

public class Move {
    public Move.Figure figure;
    public Position newPosition;

    public enum Figure {
        KING,
        ROOK;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}

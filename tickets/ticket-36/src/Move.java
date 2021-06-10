public class Move {
    Move.Figure figure;
    Position newPosition;

    public enum Figure {
        KING,
        ROOK;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}

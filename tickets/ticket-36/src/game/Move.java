package game;

public class Move {
    public Move.Figure figure;
    public Position newPosition;

    public Move(Figure figure, Position newPosition) {
        this.figure = figure;
        this.newPosition = newPosition;
    }

    public enum Figure {
        KING,
        ROOK;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }
}

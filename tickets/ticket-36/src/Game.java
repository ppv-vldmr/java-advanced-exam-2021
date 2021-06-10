public class Game {
    final Position whiteKing;
    final Position whiteRook;
    final Position blackKing;
    private Color color;

    public Game(Position[] positions) {
        whiteKing = positions[0];
        whiteRook = positions[1];
        blackKing = positions[2];
        color = Color.WHITE;
    }

    public boolean isFinished() {
        return color == Color.BLACK && isCheckmate();
    }

    private boolean isCheckmate() {
        return isAttackRookAndKing(blackKing, whiteRook) && isMoving();
    }

    private boolean isMoving() {
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                Position pos = new Position(blackKing.x + dx, blackKing.y + dy);
                if ((dx != 0 || dy != 0) && pos.isCorrect() && !isAttack(pos)) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean isAttackKings(Position whiteKing, Position blackKing) {
        return Math.abs(whiteKing.x - blackKing.x) <= 1 ||
                Math.abs(whiteKing.y - blackKing.y) <= 1;
    }

    static boolean isAttackRookAndKing(Position king, Position rook) {
        return rook.x == king.x || rook.y == king.y;
    }

    private boolean isAttack(Position blackKing) {
        return isAttackKings(whiteKing, blackKing) || isAttackRookAndKing(blackKing, whiteRook);
    }

    public Move whiteMove() {
        // TODO: do move
        return null;
    }

    private enum Color {
        WHITE,
        BLACK;

        Color next(Color color) {
            return color == WHITE ? BLACK : WHITE;
        }
    }
}

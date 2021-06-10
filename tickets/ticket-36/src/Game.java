public class Game {
    final Position whiteKing;
    final Position whiteRook;
    final Position blackKing;
    private Move move;

    public Game(Position[] positions) {
        whiteKing = positions[0];
        whiteRook = positions[1];
        blackKing = positions[2];
        move = Move.WHITE;
    }

    public boolean isFinished() {
        return move == Move.BLACK && isCheckmate();
    }

    private boolean isCheckmate() {
        return attackRookAndKing(blackKing, whiteRook) && isMoving(blackKing);
    }

    private boolean isMoving(Position blackKing) {
        // TODO: проверка - может ли черный король ходить
        return true;
    }

    static boolean attackKings(Position whiteKing, Position blackKing) {
        return Math.abs(whiteKing.x - blackKing.x) == 1 ||
                Math.abs(whiteKing.y - blackKing.y) == 1;
    }

    static boolean attackRookAndKing(Position king, Position rook) {
        return rook.x == king.x || rook.y == king.y;
    }

    private enum Move {
        WHITE,
        BLACK;

        Move next(Move move) {
            return move == WHITE ? BLACK : WHITE;
        }
    }
}

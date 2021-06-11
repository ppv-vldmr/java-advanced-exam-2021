package player;

import game.Game;
import game.Move;
import game.Position;

public class MegaBrain {
    Position king;
    Position rook;

    // 0 - самое начало
    // 1 - ладья не вертикали с белым королем
    // 2 - ладья находится на горизонтали от 2 до 7, X[2-7] без королей
    // 3 - ладья теперь всегда расположена по краям либо на позиции aN или hN
    // 4 - король на соседней с ладьей горизонтали
    // 4'- ладья переходит на противоположную сторону,
    //      если белый король подошел к линии горизонта и находится ближе к ладье, чем черный
    // 5 - дальше обычный алгоритм мата ладьей и королем
    int status = 0;
    int direction = 0;

    public MegaBrain(Position king, Position rook) {
        this.king = king;
        this.rook = rook;
    }

    public Move move(Position blackKing) {
        Move move;
        if (status < 4) {
            if ((move = startStrategy(blackKing)) != null) {
                return move;
            }
        }
        if (status < 5) {
            if ((move = mediumStrategy(blackKing)) != null) {
                return move;
            }
        }

        return finalStrategy(blackKing);
    }

    private Move startStrategy(Position blackKing) {
        if (status == 0) {
            if (rook.x == king.x) {
                for (int i = 0; i <= 7; i++) {
                    if (i != rook.x && !Game.isAttackKings(new Position(i, rook.y), blackKing)) {
                        rook.x = i;
                        status = 1;
                        return new Move(Move.Figure.ROOK, rook);
                    }
                }
            } else {
                status = 1;
            }
        }
        if (status == 1) {
            if (0 < rook.y && rook.y < 7 && rook.y != king.y && rook.y != blackKing.y) {
                status = 2;
            } else {
                for (int i = 2; i <= 5; i++) {
                    if (i != rook.y && i != king.y &&
                            !Game.isAttackKings(new Position(rook.x, i), blackKing)) {
                        rook.y = i;
                        status = 2;
                        return new Move(Move.Figure.ROOK, rook);
                    }
                }
                if (rook.x < 4) {
                    rook.x = 7;
                } else {
                    rook.x = 0;
                }
                return new Move(Move.Figure.ROOK, rook);
            }
        }
        if (status == 2) {
            if (rook.x == 0 || rook.x == 7) {
                status = 3;
            } else {
                if (blackKing.x >= 4) {
                    rook.x = 0;
                } else {
                    rook.x = 7;
                }
                status = 3;
                return new Move(Move.Figure.ROOK, rook);
            }
        }
        return null;
    }

    private Move mediumStrategy(Position blackKing) {
        if (Game.isAttackKings(rook, blackKing)) {
            return flipRook();
        }

        if (status == 3) {
            if (Math.abs(king.y - rook.y) == 1) {
                status = 4;
            } else {
                return moveKingToRookHor(blackKing);
            }
        }
        if (status == 4) {
            if (readyToFinalStatus(blackKing)) {
                status = 5;
                direction(blackKing);
            } else {
                if (Math.abs(rook.x - blackKing.x) > Math.abs(rook.x - blackKing.x)) {
                    return flipRook();
                } else {
                    if (king.y < rook.y) {
                        king.y++;
                    } else {
                        king.y--;
                    }
                    status = 5;
                    direction(blackKing);
                    return new Move(Move.Figure.KING, king);
                }
            }
        }
        return null;
    }

    private Move finalStrategy(Position blackKing) {
        if (Math.abs(rook.x - blackKing.x) <= 1) {
            return flipRook();
        }

        if (Math.abs(rook.y - blackKing.y) > 1) {
            rook.y = blackKing.y - direction;
            return new Move(Move.Figure.ROOK, rook);
        }

        if (king.x == blackKing.x && Math.abs(king.y - blackKing.y) == 2) {
            rook.y += direction;
            return new Move(Move.Figure.ROOK, rook);
        }

        if (Math.abs(king.y - blackKing.y) == 3) {
            if (Math.abs(king.x - blackKing.x) % 2 != 1) {
                if (new Position(king.x - 1, king.y).isCorrect()) {
                    king.x--;
                } else {
                    king.x++;
                }
            }
            king.y += direction;
            return new Move(Move.Figure.KING, king);
        }

        if (Math.abs(king.y - blackKing.y) == 2 && Math.abs(king.x - blackKing.x) == 1) {
            king.y -= direction;
            return new Move(Move.Figure.KING, king);
        }

        if (king.x < blackKing.x) {
            king.x++;
        } else if (king.x > blackKing.x) {
            king.x--;
        }
        if (Math.abs(rook.y - king.y) > 1) {
            king.y += direction;
        }
        return new Move(Move.Figure.KING, king);
    }

    private void direction(Position blackKing) {
        if (blackKing.y > rook.y) {
            direction = 1;
        } else {
            direction = -1;
        }
    }

    private boolean readyToFinalStatus(Position blackKing) {
        return (blackKing.y - rook.y) * (king.y - rook.y) < 0;
    }

    private Move moveKingToRookHor(Position blackKing) {
        int k = 1;
        if (king.y > rook.y) {
            k = -1;
        }
        for (int i = king.x - 1; i <= king.x + 1; i++) {
            Position pos = new Position(i, king.y + k);
            if (pos.isCorrect() && !Game.isAttackKings(pos, blackKing)) {
                king.x = pos.x;
                king.y = pos.y;
                return new Move(Move.Figure.KING, king);
            }
        }
        int r = 1;
        if (rook.x == 0) {
            king.x--;
        } else {
            king.x++;
        }
        return new Move(Move.Figure.KING, king);
    }

    private Move flipRook() {
        if (rook.x == 0) {
            rook.x = 7;
        } else if (rook.x == 7) {
            rook.x = 0;
        } else {
            throw new IllegalArgumentException("In flipRook rook.x not right");
        }
        return new Move(Move.Figure.ROOK, rook);
    }

    private int rookHorizonte(Position blackKing) {
        return blackKing.y - chooseVector(blackKing);
    }

    private int chooseVector(Position blackKing) {
        return blackKing.y < 4 ? -1 : 1;
    }
}


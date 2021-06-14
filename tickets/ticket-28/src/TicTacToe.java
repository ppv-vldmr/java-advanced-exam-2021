package src;

import java.util.Arrays;

class TicTacToe {
    private final static char EMPTY = '.';
    private final static char X = 'X';
    private final static char O = 'O';

    private final int size = 3;
    private char currentTurn = 'X';
    private Character[][] board;

    TicTacToe() {
        board = new Character[this.size][this.size];
        empty();
    }

    void empty() {
        for (final Character[] row : board) {
            Arrays.fill(row, EMPTY);
        }
    }

    private Result checkBoard() {
        int diag1 = 0, diag2 = 0, empty = 0;

        for (int i = 0; i < size; ++i) {
            int row = 0, col = 0;

            for (int j = 0; j < size; ++j) {
                if (board[i][j] == currentTurn) {
                    row++;
                }
                if (board[j][i] == currentTurn) {
                    col++;
                }
                if (board[i][j] == EMPTY) {
                    empty++;
                }
            }

            if (row == size || col == size) {
                return Result.WIN;
            }
            if (board[i][i] == currentTurn) {
                diag1++;
            }
            if (board[i][size - 1 - i] == currentTurn) {
                diag2++;
            }
        }

        if (diag1 == size || diag2 == size) {
            return Result.WIN;
        }

        if (empty == 0) {
            return Result.DRAW;
        }

        currentTurn = currentTurn == X ? O : X;

        return Result.UNKNOWN;
    }

    void isCorrectMove(int x, int y) {
        if (x < 1 || y < 1 || x > size || y > size) {
            throw new TicTacToeException("Target cell is out of board");
        }

        if (board[x - 1][y - 1] != EMPTY) {
            throw new TicTacToeException("Target cell is not empty");
        }

    }

    Result makeMove(int x, int y) {
        board[x - 1][y - 1] = currentTurn;

        return checkBoard();
    }

    Character getCurrentTurn() {
        return currentTurn;
    }

    @Override
    public String toString() {
        return  "\n   1   2   3 \n" +
                String.format("1  %c | %c | %c \n", board[0][0], board[0][1], board[0][2]) +
                "  ---|---|--- \n" +
                String.format("2  %c | %c | %c \n", board[1][0], board[1][1], board[1][2]) +
                "  ---|---|--- \n" +
                String.format("3  %c | %c | %c \n", board[2][0], board[2][1], board[2][2]);
    }
}

import game.Game;
import game.Move;
import game.Position;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Main {
    ResourceBundle usageResourceBundle;
    BufferedReader br;

    private void run(Locale locale) {
        usageResourceBundle = ResourceBundle.getBundle("bundle.UsageResourceBundle", locale);
        br = new BufferedReader(new InputStreamReader(System.in));

        Position[] positions = new Position[3];
        readFigure("white' king", positions, 0);
        readFigure("white' rook", positions, 1);

        do {
            readFigure("black' king", positions, 2);
        } while (!isCorrectBlackKingPosition(positions));


        Game game = new Game(positions);
        while (!game.isFinished()) {
            Move whiteMove = game.whiteMove();
            printMove(whiteMove);

            if (game.isFinished()) {
                print("game over");
            }

            System.out.printf("%s : %s - `%s`, %s - `%s`, %s - `%s`",
                    usageResourceBundle.getString("position now"),
                    usageResourceBundle.getString("white king"),
                    positions[0].toString(),
                    usageResourceBundle.getString("white rook"),
                    positions[1].toString(),
                    usageResourceBundle.getString("black king"),
                    positions[2].toString());
            Position oldPosition = game.getBlackKingPosition();
            do {
                readFigure("black' king", positions, 2);
            } while (!isCorrectBlackKingPosition(positions) || !isMoveCorrect(positions[2], oldPosition));
        }
    }

    private boolean isMoveCorrect(Position a, Position b) {
        if (Math.max(Math.abs(a.x - b.x), Math.abs(a.y - b.y)) != 1) {
            printError("wrong move");
            return false;
        }
        return true;
    }

    private boolean isCorrectBlackKingPosition(Position[] positions) {
        if (Game.isAttackKings(positions[0], positions[2]) || Game.isAttackRookAndKing(positions[2], positions[1])) {
            printError("king under attack");
            return false;
        }
        return true;
    }

    private void readFigure(String figureName, Position[] positions, int i) {
        String input = "";
        do {
            printEnterPos(figureName);
            try {
                input = br.readLine();
            } catch (IOException e) {
                printError("read error");
            }
        } while (!isCorrectPosition(input, positions, i));
    }

    private boolean isCorrectPosition(String input, Position[] positions, int i) {
        if (!Pattern.matches("[a-h][1-8]", input)) {
            printError("pos format");
            return false;
        }

        Position position = new Position(input);
        for (int j = 0; j < i; j++) {
            if (positions[j].equals(position)) {
                printError("equals position");
                return false;
            }
        }
        positions[i] = position;
        return true;
    }

    public static void main(String[] args) {
        switch (args.length) {
            case 0 -> new Main().run(Locale.getDefault());
            case 1 -> {
                Locale locale = new Locale(args[0]);
                new Main().run(locale);
            }
            default -> System.err.println("Wrong arguments format. Expected: `Main [locale]`");
        }
    }

    private void printEnterPos(String figureName) {
        System.out.printf("%s %s%n",
                usageResourceBundle.getString("enter pos"),
                usageResourceBundle.getString(figureName));
    }

    private void print(String output) {
        System.out.println(usageResourceBundle.getString(output));
    }

    private void printError(String output) {
        System.err.println("ERROR: " + usageResourceBundle.getString(output));
    }

    private void printMove(Move whiteMove) {
        System.out.printf("%s %s `%s`",
                usageResourceBundle.getString(whiteMove.figure.toString()),
                usageResourceBundle.getString("moving to"),
                usageResourceBundle.getString(whiteMove.newPosition.toString()));
    }
}
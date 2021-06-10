import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main {
    private void run(Locale locale) {
        usageResourceBundle = ResourceBundle.getBundle("UsageResourceBundle", locale);
        br = new BufferedReader(new InputStreamReader(System.in));

        Position[] positions = new Position[3];
        readFigure("white' king", positions, 0);
        readFigure("white' rook", positions, 1);
        do {
            readFigure("black' king", positions, 2);
        } while(checkBlackKing(positions));
    }

    private boolean checkBlackKing(Position[] positions) {
        // TODO: проверка на находится ли черный король под боем
        return true;
    }

    ResourceBundle usageResourceBundle;
    BufferedReader br;

    private void readFigure(String figureName, Position[] positions, int i) {
        String input = "";
        do {
            printEnterPos(figureName);
            try {
                input = br.readLine();
            } catch (IOException e) {
                printError("read error");
            }
        } while (checkPosition(input, positions, i));
    }

    private boolean checkPosition(String input, Position[] positions, int i) {
        if (input.matches("[1-8a-h]")) {
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
            case 0 -> {
                new Main().run(Locale.getDefault());
            }
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
        System.err.println(usageResourceBundle.getString(output));
    }
}
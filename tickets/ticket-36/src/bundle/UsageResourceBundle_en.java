package bundle;

import java.util.ListResourceBundle;

public class UsageResourceBundle_en extends ListResourceBundle {
    private final Object[][] CONTENTS = {
            {"argErr", "Wrong arguments format. Expected: `Main [locale]`"},
            {"read error", "Input reading exception"},
            {"enter pos", "Enter position of"},
            {"white' king", "white king"},
            {"white' rook", "white rook"},
            {"black' king", "black king"},
            {"white king input", "Enter position of the white king"},
            {"white king input", "Enter position of the white rook"},
            {"white king input", "Enter position of the black king"},
            {"pos format", "Incorrect format position. Expected: [a-h][1-8].\n For example, `e2`"},
            {"equals position", "Figure position cannot be equals with other"},
            {"king under attack", "The black king cannot be under attack before whites move"},
            {"game over", "Checkmate to black. Game over"},
            {"moving to", "moving to"},
            {"king", "king"},
            {"rook", "rook"},
            {"wrong move", "king cannot moving more than one cell"},
            {"white king", "white king"},
            {"white rook", "white rook"},
            {"black king", "black king"},
            {"position now", "Positions is"},
            {"error", "ERROR"},
            {"draw", "Draw"}
    };

    @Override
    protected Object[][] getContents() {
        return CONTENTS;
    }
}

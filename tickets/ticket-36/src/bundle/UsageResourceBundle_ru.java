package bundle;

import java.util.ListResourceBundle;

public class UsageResourceBundle_ru extends ListResourceBundle {
    private final Object[][] CONTENTS = {
            {"argErr", "Неверный формат аргументов. Ожидалось: `Main [locale]`"},
            {"read error", "Ошибка чтения"},
            {"enter pos", "Введите позицию"},
            {"white' king", "белого короля:"},
            {"white' rook", "белой ладьи:"},
            {"black' king", "черного короля:"},
            {"pos format", "Неверный формат позиции фигуры. Ожидалось: [a-h][1-8]. Например, `e2`"},
            {"equals position", "Позиция фигуры не может совпадать с другими"},
            {"king under attack", "Черный король не может находится под боем перед ходом белых"},
            {"game over", "Мат черным! Игра окончена"},
            {"moving to", "ходит на"},
            {"king", "король"},
            {"rook", "ладья"},
            {"wrong move", "король не может ходить больше, чем на одну клетку"},
            {"not move", "король не может не ходить. Пожалуйста, введите корректный ход"},
            {"white king", "белый король"},
            {"white rook", "белой ладья"},
            {"black king", "черный король"},
            {"position now", "Текущие позиции"},
            {"error", "ОШИБКА"},
            {"draw", "Ничья"}
    };

    @Override
    protected Object[][] getContents() {
        return CONTENTS;
    }
}

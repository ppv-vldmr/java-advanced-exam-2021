import java.util.ListResourceBundle;

public class UsageResourceBundle_ru extends ListResourceBundle {
    private final Object[][] CONTENTS = {
            {"argErr", "Неверный формат аргументов. Ожидалось: `Main [locale]`"},
            {"read error", "Ошибка чтения"},
            {"enter pos", "Введите позицию"},
            {"white' king", "белого короля:"},
            {"white' rook", "белой ладьи:"},
            {"black' king", "черного короля:"},
            {"pos format", "Неверный формат позиции фигуры. Ожидалось: [a-h1-8]. Например, `e2`"},
            {"equals position", "Позиция фигуры не может совпадать с другими"},
            {"king under attack", "Черный король не может находится под боем перед ходом белых"}
    };

    @Override
    protected Object[][] getContents() {
        return CONTENTS;
    }
}

# Билет 7. Отношения классов

## Условие

Программа должна для классов двух объектов вывести отношение между ними.

### Функциональность  
•	Поддерживаемые отношения:

        o	Совпадение
        o	Определение в одном пакете
        o	Один — предок другого
        o	Общий предок
        o	Список общих интерфейсов
•	Классы задаются во входном файле полными именами.

### Технологии  
•	Reflection  
•	I/O


## Комментарии по работе

Решение содержит один класс `ClassesRelationShips`, принимающий один аргумент в виде строки с названием файла.  
В файле содержатся строки с полными именами двух классов, разделенных между собой пробелом.

Функционал главных методов:  
- `checkCoincidence` проверяет данные классы на совпадение
- `checkSamePackage` на опеределение в одном пакете
- `findAncestor` определяет предка из двух классов
- `findCommonAncestor` ищет общего предка (в т.ч. список общих интерфейсов)

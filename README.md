# Репозиторий для решения билетов по курсу Технологии программирования

## Ссылки

### Новым участникам

* [Регистрационная форма](https://forms.gle/t78qu2zSpppf18S19)

### Работа над билетами

* [Банк билетов](https://disk.yandex.ru/i/G-TRHWkG3KeqYg)
* [Табличка с распределением билетов](https://disk.yandex.ru/i/wiryl-VECrACmw)
* [Участники](https://docs.google.com/spreadsheets/d/1kv4H8NkwP1sX_45K3ttMeWkVlelh2iEnebFutuWSn6s/edit?resourcekey#gid=483376745)
* [Дискорд](https://discord.gg/n6JqkSps)

### Экзамен

* [Правила сдачи экзамена](https://docs.google.com/document/d/e/2PACX-1vSN52k6vkuNNLXy_fZzCNShg5i5tyRKQ0BLqXTDnAH5g4xGk8HTZbyObjPuRsBHB8NOBE_lxgcZfQ15/pub)

### Дополнительные ресурсы

* [Технологии](https://onedrive.live.com/edit.aspx?resid=B866206ADE6E14DD!467&ithint=file%2cdocx&authkey=!AO6pyku79veei_I)
* [Проверка текстов на схожесть](https://petr-panda.ru/sravnvit-teksty/)


# ОБЯЗАТЕЛЬНО К ПРОЧТЕНИЮ

Наша основная задача - успешно сдать экзамен по java. Сделать целиком свой билет во время экзамена практически невозможно, именно поэтому была создана наша команда, которая заранее подготовит билеты, и на экзамене каждый возьмет готовое решение. С одной стороны, звучит здорово, а с другой стороны, вероятность словить бан (-20 баллов) становится намного выше. Чтобы избежать наихудшего исхода, каждый участник __обязуется__ следовать правилам, указанным ниже. Также, учитывая, что много людей будет работать с одним репозиторием, были сформированы правила, цель которых стандартизировать общую работу, об этом также ниже.

Приятного ознакомления!

## Требования к участникам

- Каждый участник должен __как минимум__ выполнить один билет и написать к нему тесты.

Тесты нужны для увеличения гарантий, что написанный код ожидаемо работает и удовлетворяет условию билета (во время экзамена времени на полное переделывание тикета не будет).

Тестов очень много не надо - достаточно небольших, которые проверяют, что вспомогательные функции ожидаемо работают, и целиком программа ведет себя надлежащим образом.

- __Запрещается__ делиться билетами и решениями до и после сдачи экзамена.

Такая предосторожность нужна, чтобы никто иной не поделился с другим, другой не поделилися с третьим, и по итогу все не дошло до преподавателей, и билеты были полностью изменены, что сведет всю проделанную работу на нет.

Делясь решениями, вы подставляете и себя, и сокомандников, и человека, с которым делитесь, потому что вероятность словить бан существенно возрастает.

- __Необходимо__ следовать правилам работы с репозиторием.

Это нужно для поддержания порядка и консистентности.

Если где-то возникли вопросы - смело пишите в чат, обязательно поможем.

- Если хотите добавить друга в нашу команду - __прежде всего__ расскажите ему про требования к участникам. Если он согласен им следовать - можете добавить, пока общее количество участников <= __25__. Если не готов, то в нашей команде ему, к сожалению, не место.

На экзамене максимум будет 1 билет у четверых. Число __25__ составляет 75% от общего количества билетов и понижает вероятность получить одинаковые билеты, следовательно, вероятность получить бан существенно ниже.

## Работа с билетами

Прежде чем выбрать билет вы смотрите в [таблицу](https://disk.yandex.ru/i/wiryl-VECrACmw), выбираете незанятый, проставляя свой ник телеграма в столбце _Выполняет_ и не забываете проставить соответствующий цвет в столбце _Статус_. Если вы заранее знаете проверяющего, то можете проставить и его.

Если вы хотите проверить чье-то решение, то проставляйте себя в столбце _Проверяющие_ и в PR в разеделе _Reviewers_, который соответсвует тикету.

Если возник какой-то вопрос по решению - смело спрашивайте об этом в чате.

Для этого рекомендуется использовать хештеги, чтобы можно было понять, с чем связан ваш вопрос:

`#help-<номер тикета>` - вопрос по тикету. Для обсуждения можно пойти в наш дискорд канал - так будет проще остальным присоединиться к обсуждению и решить проблему. Для ответа на вопрос используйте `Reply`, чтобы можно было проследить историю ответов и понять, получилось ли помочь.

`#ready-for-review--<номер тикета>` - тикет готов к проверке, тесты к нему написаны.

Не забывайте менять статус билетов в таблице, чтобы понимать, в каком состоянии, что находится, какой тикет можно взять, кому нужна помощь.

## Работа с репозиторием

Папка `tickets` содержит решения различных билетов.

Формат папки с решением билета (`ticket-<номер билета>`):  
- файл `README.md` должен содержать условие билета и описания реализованных классов
- если описание классов находится внутри кода через комментарии/java-doc, то напишите внутри `README.md` об этом
- папка `src`, в которой будет находиться код решения и тесты

### Тесты

Для написания тестов используйте `JUnit`. Если его функционала недостаточно, или вам по душе какой-то иной фреймворк - необходимо добавить ссылки на ознакомление с ним в документ [Технологии](https://onedrive.live.com/edit.aspx?resid=B866206ADE6E14DD!467&ithint=file%2cdocx&authkey=!AO6pyku79veei_I) и быть готовым рассказать, как ведет тот или иной метод.

Файл с тестами должен находиться в директории `src`, рядом с файлами кода по билету, и называться `Tests.java`.

### PR

Каждый билет будет иметь соответствующий PR (pull request): для каждого билета заводите PR с веткой `ticket-<номер тикета>`. PR называете также `ticket-<номер тикета>`. Если вы заранее договорились с кем-то о том, что он проверит ваш тикет, проставляйте его в _Reviewers_.

Если нет - проверяющий самостоятельно проставляет туда себя.

Внутри вашего тикета разрешатся менять __только__ содержимое папки `ticket-<номер вашего тикета>`, чтобы избежать конфликтов с другими.

Ожидается, что в основном репозитории будут только файлы `README.md`, код и тесты - другие файлы запрещается добавлять.

Внутри PR будет обсуждение написанного кода. Всякие проблемы, вопросы - описываете внутри PR.

__Запрещается__ мерджить с главной веткой. 

[Пример PR](https://github.com/ppv-vldmr/java-advanced-exam-2021/pull/1)

### Ожидаемая дата массового мерджа - 13 июня, вечер

## Дискорд

Используем для обсуждения вопросов по тикетам, для объяснения технологий, используемых внутри решения.

## План работы во время экзамена

Вы узнаёте свой билет и после этого заходите в голосовой канал нашего дискорда, который соответствует вашему тикету. Далее обсуждаете с людьми, которым попался тот же билет, каким образом вы измените итоговое решение. Далее можно проверить ваши кода на схожесть, используя сайт из раздела дополнительные ресурсы.

В итоговый репозиторий с решениями не добавляйте `Tests.java` и не забудьте подправить `README.md`, чтобы он соответствовал вашему исправленному варианту.

## ВАЖНО ПОМНИТЬ

Все держится на честности, порядочности и благоразумии, поэтому, пожалуйста, отнеситесь к правилам внимательно и не нарушайте их. Спасибо!

Если после ознакомления остались вопросы - пишите о них в нашем тг чате.

## Билеты:  
- [Билет 1. Менеджер строчек. Ядро](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-01)
- [Билет 2. Менеджер строчек. Редактор](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-02)
- [Билет 3. Менеджер строчек. Statistics Analyzer](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-03)
- [Билет 4. Менеджер строчек. Query Tool](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-04)
- [Билет 5. Исполнитель](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-05)
- [Билет 6. Устойчивая сортировка](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-06)
- [Билет 7. Отношения классов](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-07)
- [Билет 8. File Manager](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-08)
- [Билет 9. TCP Proxy](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-09)
- [Билет 10. UDP Proxy](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-10)
- [Билет 11. Автоматический переводчик](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-11)
- [Билет 12. Bag](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-12)
- [Билет 13. LinkedBag](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-13)
- [Билет 14. Менеджер плагинов](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-14)
- [Билет 15. Memorizer](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-15)
- [Билет 16. Ленивый список](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-16)
- [Билет 17. Перекодировка email](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-17)
- [Билет 18. Tracing Proxy](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-18)
- [Билет 19. Обработка файла](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-19)
- [Билет 20. Очередь заданий](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-20)
- [Билет 21. Тараканьи бега](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-21)
- [Билет 22. Обобщенные матрицы](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-22)
- [Билет 23. Chat Client](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-23)
- [Билет 24. Chat Server](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-24)
- [Билет 25. TODO List](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-25)
- [Билет 26. Markdown2HTML](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-26)
- [Билет 27. Wiki2HTML](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-27)
- [Билет 28. Сетевые крестики-нолики](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-28)
- [Билет 29. Sort](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-29)
- [Билет 30. Parallel MergeSort](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-30)
- [Билет 31. Parallel QuickSort](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-31)
- [Билет 32. Java Shell](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-32)
- [Билет 33. Протоколирование](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-33)
- [Билет 34. Конвертация файла](https://github.com/ppv-vldmr/java-advanced-exam-2021/tree/master/tickets/ticket-34)

# CLI
Простой интерпретатор командной строки, поддерживающий команды:

- cat [FILE] - выводит содержимое файла на экран
- echo - выводит на экран свои аргумент
- wc [FILE] - выводит количество строк, слов и байт в файле
- pwd - выводит текущую директорию
- exit - выходит из интерпретатора

## Арxитектура
- interfece Command имеет единственный метод run, который отвечает за выполнение команды.
- Классы команд: Cat, Echo, Pwd, Wc реализуют интерфейс Command
- Класс CommandBuilder получает на вход имя команды и возвращает экземпляр нужного класса или null, если такая команда не была реализована.
- Класс Parser отвечает за парсинг входной строки
- Класс ShellCommand отвечает за вызов консольных команд, необходим для тестов и в случае, если команда не была найдена.
- Класс Variables отвечает за хранение переменных.
- Класс CommandLine - mainClass
    - Считывает пользовательсий ввод, разбивает строку по |.
	- Запускает Parser на каждом элементе (получившемся после разбиения строки).
	- Получаем из Parser имя команды и аргументы.
	- С помощью CommandBuilder получаем экземпляр класса нужной команды или null
	  - Если получили null, то пытаемся с помощью ShellCommand запустить команду.
	- Запускаем метод run у экземпляра класса команды и выводим результат.
	- Так повторяем, пока не считаем exit.

## Запуск
./gradlew build

./gradlew run

./gradlew test - для запуска тестов


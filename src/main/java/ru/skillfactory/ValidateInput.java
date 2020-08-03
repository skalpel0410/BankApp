package ru.skillfactory;

/**
 * Этот класс обновлённая версия вашего консольного ввода, здесь не просто спрашивайте ввод, а проверяйте его.
 * Главная задача данного класса решить проблему ошибок ввода пользователем.
 *
 * Метод askInt должен точно возвращать число и не генерировать exceptions.
 * Требования к askStr сформулируйте сами или не переопределяйте его если с ним проблем не увидите.
 */
public class ValidateInput extends ConsoleInput {

    @Override
    public String askStr(String question) {
        return super.askStr(question);
    }

    @Override
    public int askInt(String question) {
        return super.askInt(question);
    }
}

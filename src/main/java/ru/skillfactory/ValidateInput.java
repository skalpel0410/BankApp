package ru.skillfactory;

import java.util.Scanner;

/**
 * Этот класс обновлённая версия вашего консольного ввода, здесь не просто спрашивайте ввод, а проверяйте его.
 * Главная задача данного класса решить проблему ошибок ввода пользователем.
 *
 * Метод askInt должен точно возвращать число и не генерировать exceptions.
 * Требования к askStr сформулируйте сами или не переопределяйте его если с ним проблем не увидите.
 */
public class ValidateInput extends ConsoleInput {
    private final Scanner scanner = new Scanner(System.in);
    /**
     * Сами решите необходимо ли переопределение этого метода.
     */
    @Override
    public String askStr(String question) {
        System.out.print(question);
        return scanner.nextLine();
    }

    /**
     * Необходимо ли переопределять метод, или убрать переопределение вы решаете сами.
     * На данный момент в этот методе может произойти непроверяемое исключение.
     */
    @Override
    public int askInt(String question) {
        try {
            return Integer.parseInt(askStr(question));
        } catch (NumberFormatException e) {}
        return -1;
    }

    /**
     * Сами решите необходимо ли переопределение этого метода.
     */
    @Override
    public long askLong(String question) {
        try {
            return Long.parseLong(askStr(question));
        } catch (NumberFormatException e) {}
        return -1;
        }
    }


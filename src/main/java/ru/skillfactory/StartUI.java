package ru.skillfactory;

import ru.skillfactory.actions.*;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Класс, который запускает общение с пользователем.
 */
public class StartUI {

    /**
     * Здесь будет происходить инициализация меню
     *  1. Авторизация пользователя.
     *  2. Печать меню.
     *  3. В зависимости от введённого числа запуск нужной функции.
     *
     * @param bankService BankService объект.
     * @param actions массив с действиями.
     * @param input Input объект.
     */
    public void init(BankService bankService, UserAction[] actions, Input input, Logger logger) {
        String requisite = authorization(bankService, input, logger);
        showMenu(actions);
        boolean run = true;
        while (run) {
            int select = input.askInt("Выберите пункт меню: ");
            // Здесь такой if, который не даст выйти в ArrayIndexOutOfBoundsException.
            if (select >= 0 && select <= actions.length - 1) {
                // Мы по индексу массива вызываем метод execute нашего Action-объекта.
                run = actions[select].execute(bankService, input, requisite, logger);
            } else {
                System.out.println("Такого пункта нету...");
            }
        }
    }


    /**
     * Метод работает пока пользователь не авторизуется.
     *
     * @param bankService BankService объект.
     * @param input Input объект.
     * @return возвращает реквизиты аккаунта, под которым авторизовался пользователь.
     *
     */
    private String authorization(BankService bankService, Input input, Logger logger) {
        String rsl = null;
        boolean authComplete = false;
        while (!authComplete) { // цикл отключён!!!
            /*
             * Запрашиваеем у пользователя логин, пароль пока он не пройдёт авторизацию.
             * Авторизация пройдена при условие, что в BankService есть пользователь с
             * данным логином и паролем.
             */
            String login = input.askStr("Ваш логин: ");
            String password = input.askStr("Ваш password: ");
            if (bankService.getRequisiteIfPresent(login, password, logger).isPresent()) {
                authComplete = true;
                rsl = bankService.getRequisiteIfPresent(login, password, logger).get();
            } else {
                System.out.println("Введены неверные авторизационные данные. Попробуйте еще раз.");
            }
        }
        return rsl;
    }

    /**
     * Печатается меню пользователя.
     *
     * @param actions массив с действиями.
     */
    private void showMenu(UserAction[] actions) {
        System.out.println("Menu.");
        for (int index = 0; index < actions.length; index++) {
            System.out.println(index + ". " + actions[index].getTitle());
        }
    }

    public static void main(String[] args) {
        // логирование
        Logger logger = Logger.getLogger(BankService.class.getName());
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler("MyLogFile.log");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (SecurityException e){
            logger.log(Level.SEVERE, "Не удалось создать файл лога из-за политики безопасности", e);
        } catch(
                IOException e)

        {
            logger.log(Level.SEVERE, "Не удалось создать файл лога из-за ошибки ввода-вывода", e);
        }



        BankService bankService = new BankService();
        //заполнение тестовыми данными
        bankService.addAccount(new BankAccount("Вася", "123", "1"));
        bankService.addAccount(new BankAccount("Петя", "123", "2"));
        bankService.addAccount(new BankAccount("Маша", "123", "3"));
        bankService.addAccount(new BankAccount("Саша", "123", "4"));
        bankService.addAccount(new BankAccount("Оля", "123", "5"));

        // В массиве хранятся объекты, которые представляют наши действия.
        UserAction[] actions = {
                new ShowBalanceAction(),
                new TopUpBalanceAction(),
                new TransferToAction(),
                new Exit()
        };
        // создаем экземпляр ValidateInput
        Input input = new ValidateInput();
        // Запускаем наш UI передавая аргументами банковский сервис, экшены и Input.
        new StartUI().init(bankService, actions, input, logger);
    }
}

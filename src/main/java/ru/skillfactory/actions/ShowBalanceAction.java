package ru.skillfactory.actions;

import ru.skillfactory.*;

import java.util.logging.Logger;

/**
 * Класс для реализации действия "Показать баланс", используется в StartUI.
 */
public class ShowBalanceAction implements UserAction {

    @Override
    public String getTitle() {
        return "Показать баланс";
    }

    /**
     * @param bankService BankService объект.
     * @param input Input объект.
     * @param requisite Строка в произвольной форме, используется для поиска пользователя.
     * @return возвращает всегда true, приложение продолжает работать.
     */
    @Override
    public boolean execute(BankService bankService, Input input, String requisite, Logger logger) {
        System.out.println("Ваш баланс равен: " + bankService.balance(requisite, logger)/100);
        return true;
    }
}

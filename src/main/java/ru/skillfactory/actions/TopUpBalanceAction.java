package ru.skillfactory.actions;

import ru.skillfactory.*;

import java.util.logging.Logger;

/**
 * Класс для реализации действия "Пополнить баланс", используется в StartUI.
 */
public class TopUpBalanceAction implements UserAction {

    @Override
    public String getTitle() {
        return "Пополнить баланс";
    }

    /**
     *
     * @param bankService BankService объект.
     * @param input       Input объект.
     * @param requisite   Строка в произвольной форме, используется для поиска пользователя.
     * @return возвращает всегда true, приложение продолжает работать.
     */
    @Override
    public boolean execute(BankService bankService, Input input, String requisite, Logger logger) {
        long amount = 0L;
        boolean isCorrect = false;
        while (!isCorrect) {
            amount = input.askLong("Введите сумму пополнения: ");
            if (amount < 0) {
                System.out.println("Введены неверные данные. Попробуйте еще раз.");
            } else {
                isCorrect = true;
            }
        }
        bankService.topUpBalance(requisite, amount, logger);
        System.out.println("Баланс успешно пополнен и составляет: " + bankService.balance(requisite, logger)/100);
        return true;
    }
}

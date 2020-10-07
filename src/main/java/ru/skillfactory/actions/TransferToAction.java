package ru.skillfactory.actions;

import ru.skillfactory.*;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Класс для реализации действия "Перевести средства", используется в StartUI.
 */
public class TransferToAction implements UserAction {

    @Override
    public String getTitle() {
        return "Перевести средства";
    }

    /**
     * Перевести средства - так как операция важная заставляем вводить пароль.
     *
     * @param bankService BankService объект.
     * @param input       Input объект.
     * @param requisite   Строка в произвольной форме, используется для поиска пользователя.
     * @return возвращает всегда true, приложение продолжает работать.
     */
    @Override
    public boolean execute(BankService bankService, Input input, String requisite, Logger logger) {
        String username = bankService.getNameByRequisite(requisite, logger).get();
        //валидация получателя
        boolean isDestCorrect = false;
        Optional<String> destOptional = Optional.of("");
        while (!isDestCorrect) {
            destOptional = bankService.getRequisiteByName(input.askStr("Введите имя получателя: "), logger);
            if (destOptional.isPresent()) {
                isDestCorrect = true;
            } else {
                System.out.println("Получатель с таким именем не найден. Попробуйте еще раз.");
            }
        }
        String destRequisite = destOptional.get();
        //валидация суммы
        boolean isAmountCorrect = false;
        long amount = -1;
        while (!isAmountCorrect) {
            amount = input.askLong("Введите сумму перевода: ");
            if (amount >= 0) {
                if (bankService.balance(requisite, logger) >= amount*100) {
                    isAmountCorrect = true;
                } else {
                    System.out.println("На Вашем счете недостаточно средств для перевода. Попробуйте еще раз");
                }
            } else {
                System.out.println("Некорректно введенная сумма. Попробуйте еще раз.");
            }
        }
        String password = input.askStr("Введите свой пароль для подтверждения перевода: ");
        if (bankService.getRequisiteIfPresent(username, password, logger).isPresent()) {

            bankService.transferMoney(username, password, requisite, destRequisite, amount, logger);
            System.out.println("Перевод успешно произведен. Остаток средств на Вашем балансе:" + bankService.balance(requisite, logger)/100);
        } else {
            System.out.println("Пароль неверен. Транзакция отклонена.");
        } return true;


    }
}

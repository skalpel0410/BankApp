package ru.skillfactory.actions;

import ru.skillfactory.*;

import java.util.Optional;

/**
 * Класс для реализации действия "Перевести средства", используется в StartUI.
 */
public class TransferToAction implements UserAction {

    @Override
    public String getTitle() {
        return "Перевести средства";
    }

    /**
     * Перевести средства - также общаетесь в этом методе с пользователем и передаёте информацию,
     * так как операция важная желательно ещё раз заставлять вводить пароль/логин и передавать информацию
     * в BankService. Exceptions пользователю печатать не надо (как и в других методах этого класса),
     * вводите подсказки или написанные вами сообщения об ошибках.
     *
     * @param bankService BankService объект.
     * @param input       Input объект.
     * @param requisite   Строка в произвольной форме, используется для поиска пользователя.
     * @return возвращает всегда true, приложение продолжает работать.
     */
    @Override
    public boolean execute(BankService bankService, Input input, String requisite) {
        String username = bankService.getNameByRequisite(requisite).get();
        //валидация получателя
        boolean isDestCorrect = false;
        Optional<String> destOptional = Optional.of("");
        while (!isDestCorrect) {
            destOptional = bankService.getRequisiteByName(input.askStr("Введите имя получателя: "));
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
            amount = input.askLong("Введите сумму перевода");
            if (amount >= 0) {
                if (bankService.balance(requisite) >= amount*100) {
                    isAmountCorrect = true;
                } else {
                    System.out.println("На Вашем счете недостаточно средств для перевода. Попробуйте еще раз");
                }
            } else {
                System.out.println("Некорректно введенная сумма. Попробуйте еще раз.");
            }
        }
        String password = input.askStr("Введите свой пароль для подтверждения перевода");
        if (bankService.getRequisiteIfPresent(username, password).isPresent()) {

            bankService.transferMoney(username, password, requisite, destRequisite, amount);
            System.out.println("Перевод успешно произведен. Остаток средств на Вашем балансе:" + bankService.balance(requisite)/100);
        } else {
            System.out.println("Пароль неверен. Транзакция отклонена.");
        } return true;


    }
}

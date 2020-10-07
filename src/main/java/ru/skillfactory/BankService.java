package ru.skillfactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * BankService - класс, отвечающий за осуществление операций с аккаунтами.
 */
public class BankService {

    private final Map<String, BankAccount> accounts = new HashMap<>();

    /**
     * @param account Аккаунт с заполненными полями.
     */
    public void addAccount(BankAccount account) {
        boolean isFindAcc = false;
        for (Map.Entry<String, BankAccount> entry : accounts.entrySet()) {
            BankAccount acc = entry.getValue();
            if (acc.getUsername().equals(account.getUsername())) {
                isFindAcc = true;
                break;
            }
        }
        if (!isFindAcc) {
            accounts.putIfAbsent(account.getRequisite(), account);
        }

    }

    /**
     * Метод проверяет что в Map-е есть аккаунт, если есть вернёт реквизиты. В моей реализации
     * метод просто вернёт реквезиты без генерации исключений. Вы можете использовать подход с
     * исключениями, тогда на каждую ситуацию должно быть отдельное исключение
     *
     * @param username валидная строка.
     * @param password валидная строка.
     * @return возвращает объект Optional, который будет содержат строку - requisite,
     * если передоваемого пользователя нету или пароль не совпадает вы сможете
     * передать пустой объект Optional и проверить что он не пуст.
     */
    public Optional<String> getRequisiteIfPresent(String username, String password, Logger logger) {
        for (Map.Entry<String, BankAccount> entry : accounts.entrySet()) {
            BankAccount account = entry.getValue();
            if (account.getPassword().equals(password) & account.getUsername().equals(username)) {
                return Optional.of(entry.getKey());
            }
        }
        //логирование неудачной попытки
        logger.log(Level.WARNING, ("Неудачная попытка авторизации пользователя " + username));
        return Optional.empty();
    }

    /**
     * метод получения реквизита по имени
     *
     * @param username имя пользователя
     * @return возвращает реквизит или пустой Optional
     */
    public Optional<String> getRequisiteByName(String username, Logger logger) {
        for (Map.Entry<String, BankAccount> entry : accounts.entrySet()) {
            BankAccount account = entry.getValue();
            if (account.getUsername().equals(username)) {
                return Optional.of(entry.getKey());
            }
        }
        //логирование не найденного реквизита
        logger.log(Level.WARNING, ("Реквизит не найден " + username));
        return Optional.empty();
    }

    /**
     * метод получения имени по реквизиту
     *
     * @param requisite реквизиты пользователя
     * @return возвращает имя пользователя
     */
    public Optional<String> getNameByRequisite(String requisite, Logger logger) {
        BankAccount account = accounts.get(requisite);
        if (account != null) {
            return Optional.of(account.getUsername());
        }
        //логирование не найденного пользователя
        logger.log(Level.WARNING, ("Пользователь не найден по реквизиту " + requisite));
        return Optional.empty();
    }

    /**
     * Метод кол-во средств на передаваемых реквизитах. На этом методе вам нужно выкидывать исключение,
     * если передаваемые реквизиты не валидны, это единственный способ сообщить о проблеме.
     *
     * @param requisite реквизиты, строка в произвольном формате.
     * @return кол-во средств в копейках (для других валют аналогично было бы).
     */
    public long balance(String requisite, Logger logger) {
        BankAccount account = accounts.get(requisite);
        if (account != null) {
            // логирование запроса баланса
            logger.log(Level.FINE, ("Успешный запрос баланса по реквизиту " + requisite));
            return account.getBalance();

        } else {
            //логирование не найденного аккаунта
            logger.log(Level.WARNING, ("Неуспешный запрос баланса по реквизиту " + requisite));
            return 0;
        }
    }

    /**
     * Метод пополняет баланс.
     *
     * @param requisite реквизиты, строка в произвольном формате.
     * @param amount    сумма для пополнения.
     * @return возвращает true если баланс был увеличен.
     */
    public boolean topUpBalance(String requisite, long amount, Logger logger) {
        BankAccount account = accounts.get(requisite);
        account.setBalance(account.getBalance() + amount * 100);
        //логирование пополнения
        logger.log(Level.FINE, ("Аккаунт " + requisite + "пополнен на " + amount ));
        return true;
    }

    /**
     * Метод, если все условия соблюдены, переводит средства с одного счёта на другой.
     *
     * @param username      строка в произвольном формате.
     * @param password      строка в произвольном формате.
     * @param srcRequisite  реквизиты, строка в произвольном формате.
     * @param destRequisite реквизиты, строка в произвольном формате.
     * @param amount        кол-во средств в копейках.
     * @return true если выполнены все условия, средства фактически переведены.
     */
    public boolean transferMoney(String username, String password, String srcRequisite,
                                 String destRequisite, long amount, Logger logger) {
        boolean rsl = false;
        if (getRequisiteIfPresent(username, password, logger).isPresent() &
                getRequisiteIfPresent(username, password, logger).get().equals(srcRequisite) & //проверяем что есть счет с такой учеткой
                accounts.get(destRequisite) != null & //проверяем что есть счет с такими реквизитами
                accounts.get(srcRequisite) != null & //проверяем что есть счет с такими реквизитами
                accounts.get(srcRequisite).getBalance() >= amount * 100 & // проверяем достаточность средств у источника
                amount > 0) {
            BankAccount srcAccount = accounts.get(srcRequisite);
            BankAccount destAccount = accounts.get(destRequisite);
            srcAccount.setBalance(srcAccount.getBalance() - amount * 100);
            //логирование списания
            logger.log(Level.FINE, ("Со счета пользователя" + username + "переведено "+ amount));
            destAccount.setBalance(destAccount.getBalance() + amount * 100);
            //логирование начисления
            logger.log(Level.FINE, ("На счет пользователя" + getNameByRequisite(destRequisite, logger) + "переведено "+ amount));

            rsl = true;
        }

        return rsl;
    }
}

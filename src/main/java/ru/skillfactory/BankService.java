package ru.skillfactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * BankService - класс, который нарушает принцип единственной ответственности. У нас он сразу
 * и хранит аккаунты, и реализует логику проверки баланса и переводов. Можете использовать
 * его в текущем виде, можете решить проблему множественной ответственности и создать интерфейс
 * AccountStore, написать его реализации и в BankService передавать хранилище. В этом случае в
 * этом классе должна быть только логика переводов + баланс и вы просто обращаетесь в store, передовая
 * ответ на уровень выше.
 */
public class BankService {
    /**
     * В Map-е храните аккаунты, ключ это реквизиты
     * (они неизменяемые у аккаунтов, это важно сохранить чтобы ключ всегда был валиден).
     * <p>
     * Подумайте почему используется именно map, можно ли использовать решение лучше.
     */
    private final Map<String, BankAccount> accounts = new HashMap<>();

    /**
     * Метод добавляете аккаунт в Map-у если у аккаунта уникальные реквизиты (логин тоже нужно проверить,
     * чтобы корректно работал метод contains).
     * <p>
     * Если поймёте как использовать и верно примените Map.putIfAbsent будет очень хорошо.
     *
     * @param account Аккаунт с заполненными полями.
     */
    public void addAccount(BankAccount account) {
        Boolean isFindAcc = false;
        for (Map.Entry<String, BankAccount> entry : accounts.entrySet()) {
            BankAccount acc = entry.getValue();
            if (acc.getUsername().equals(account.getUsername())) {
                isFindAcc = true;
            }
        }
        if (!isFindAcc) {
            accounts.putIfAbsent(account.getRequisite(), account);
        }
        ;
    }

    /**
     * Метод проверяет что в Map-е есть аккаунт, если есть вернёт реквезиты. В моей реализации
     * метод просто вернёт реквезиты без генерации исключений. Вы можете использовать подход с
     * исключениями, тогда на каждую ситуацию должно быть отдельное исключение
     *
     * @param username валидная строка.
     * @param password валидная строка.
     * @return возвращает объект Optional, который будет содержат строку - requisite,
     * если передоваемого пользователя нету или пароль не совпадает вы сможете
     * передать пустой объект Optional и проверить что он не пуст.
     */
    public Optional<String> getRequisiteIfPresent(String username, String password) {
        for (Map.Entry<String, BankAccount> entry : accounts.entrySet()) {
            BankAccount account = entry.getValue();
            if (account.getPassword().equals(password) & account.getUsername().equals(username)) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }
    //метод получения реквизита по имени
    public Optional<String> getRequisiteByName(String username) {
        for (Map.Entry<String, BankAccount> entry : accounts.entrySet()) {
            BankAccount account = entry.getValue();
            if (account.getUsername().equals(username)) {
                return Optional.of(entry.getKey());
            }
        }
        return Optional.empty();
    }
    //метод получения имени по реквизиту
    public Optional<String> getNameByRequisite(String requisite) {
        BankAccount account = accounts.get(requisite);
        if (account !=null) {
            return Optional.of(account.getUsername());
        }

        return Optional.empty();
    }

    /**
     * Метод кол-во средств на передаваемых реквизитах. На этом методе вам нужно выкидывать исключение,
     * если передаваемые реквизиты не валидны, это единственный способ сообщить о проблеме.
     *
     * @param requisite реквизиты, строка в произвольном формате.
     * @return кол-во средств в копейках (для других валют аналогично было бы).
     */
    public long balance(String requisite) {
        BankAccount account = accounts.get(requisite);
        if (account != null) {
            return account.getBalance();
        } else {
            return 0;
        }
    }

    /**
     * Метод должен пополнять баланс.
     *
     * @param requisite реквизиты, строка в произвольном формате.
     * @param amount    сумма для пополнения.
     * @return возвращает true если баланс был увеличен.
     */
    public boolean topUpBalance(String requisite, long amount) {
        BankAccount account = accounts.get(requisite);
        account.setBalance(account.getBalance() + amount*100);
        return true;
    }

    /**
     * Метод, если все условия соблюдены, переводит средства с одного счёта на другой.
     *
     * @param username      строка в произвольном формате.
     * @param password      строка в произвольном формате.
     * @param srcRequisite  реквизиты, строка в произвольном формате.
     * @param destRequisite реквизиты, строка в произвольном формате.
     * @param amount        кол-во средств в копейках (для других валют аналогично было бы).
     * @return true если выполнены все условия, средства фактически переведены.
     */
    public boolean transferMoney(String username, String password, String srcRequisite,
                                 String destRequisite, long amount) {
        boolean rsl = false;
        if (getRequisiteIfPresent(username, password).isPresent() &
                getRequisiteIfPresent(username, password).get().equals(srcRequisite) & //проверяем что есть счет с такой учеткой
                accounts.get(destRequisite) != null & //проверяем что есть счет с такими реквизитами
                accounts.get(srcRequisite) != null & //проверяем что есть счет с такими реквизитами
                accounts.get(srcRequisite).getBalance()>=amount*100 & // проверяем достаточность средств у источника
                amount > 0) {
            BankAccount srcAccount = accounts.get(srcRequisite);
            BankAccount destAccount = accounts.get(destRequisite);
            srcAccount.setBalance(srcAccount.getBalance() - amount*100);
            destAccount.setBalance(destAccount.getBalance() + amount*100);
            rsl = true;
        }

        return rsl;
    }
}

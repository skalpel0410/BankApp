package ru.skillfactory;

import java.util.Objects;

/**
 * Наша модель данных для работы со счётом.
 */
public class Account {

    /**
     * Для поиска получателя перевода используйте реквизиты.
     */
    private String requisite;
    /**
     * Баланс (пусть будет в копейках, при печати можете в рубли переводить, для других валют аналогично).
     * Почему long? - Прочитайте тему, вдруг когда-ни будь спросят.
     *
     * https://ru.stackoverflow.com/questions/667706/%D0%92-%D1%87%D0%B5%D0%BC-%D1%85%D1%80%D0%B0%D0%BD%D0%B8%D1%82%D1%8C-%D0%B4%D0%B5%D0%BD%D1%8C%D0%B3%D0%B8-float-double
     */
    private long balance;

    public Account(String requisite, long balance) {
        this.requisite = requisite;
        this.balance = balance;
    }

    public String getRequisite() {
        return requisite;
    }

    public void setRequisite(String requisite) {
        this.requisite = requisite;
    }

    public long getBalance() {
        return balance;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return balance == account.balance
                && Objects.equals(requisite, account.requisite);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requisite, balance);
    }
}

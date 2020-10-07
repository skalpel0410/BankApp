package ru.skillfactory.actions;

import ru.skillfactory.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс для реализации действия "Выйти из программы", используется в StartUI.
 */
public class Exit implements UserAction {

    @Override
    public String getTitle() {
        return "Выйти из программы";
    }

    /**
     * Метод обработает завершение работы приложения и возвратит false, что
     * должно в StartUI привести к завершению работы.
     *
     * @param bankService BankService объект.
     * @param input Input объект.
     * @param requisite Строка в произвольной форме, используется для поиска пользователя.
     * @return возвращает false провоцируя выход из приложения.
     */
    @Override
    public boolean execute(BankService bankService, Input input, String requisite, Logger logger) {
        logger.log(Level.FINE, ("Приложение завершило работу."));
        System.out.println("Приложение завершило работу.");
        return false;
    }
}

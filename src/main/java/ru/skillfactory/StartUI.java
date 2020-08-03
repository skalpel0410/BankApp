package ru.skillfactory;

import java.util.List;

/**
 * Класс, который запускает общение с пользователем.
 */
public class StartUI {
    private final BankService bankService;
    private final List<TerConsumer<BankService, Input, String>> actions;
    private final Input input;

    /**
     * Здесь будет происходить инициализация меню, вы
     *  1. Авторизовываете пользователя.
     *  2. Печатаете меню.
     *  3. В зависимости от введённого числа запускаете нужную функцию.
     */
    public void init() {
        String requisite = authorization();
        showMenu();
        int select = input.askInt("Выберите пункт меню: ");
        while (select != actions.size()) {
            if (select >= 0 && select <= actions.size() - 1) {
                // Мы обращаемся к листу и запускаем один из статических методов, которые мы хранили в листе.
                actions.get(select).accept(bankService, input, requisite);
            } else {
                System.out.println("Такого пункта нету...");
            }
            select = input.askInt("Выберите пункт меню: ");
        }
        System.out.println("Попрощались здесь...");
    }

    /**
     * Конструктор со всеми параметрами.
     *
     * @param bankService банковский сервис
     * @param actions лист со списком действий
     * @param input одна из реализаций интерфейса
     */
    public StartUI(BankService bankService, List<TerConsumer<BankService, Input, String>> actions, Input input) {
        this.bankService = bankService;
        this.actions = actions;
        this.input = input;
    }

    /**
     * Метод должен работать пока пользователь не авторизуется (пока отключил цикл!).
     *
     * @return возвращает реквизиты аккаунта, под которым авторизовался пользователь.
     */
    public String authorization() {
        String rsl = null;
        boolean authComplete = true;
        while (!authComplete) { // цикл отключён!!!
            /*
             * Запрашиваете у пользователя логин, пароль пока он не пройдёт авторизацию.
             * Авторизация пройдена при условие что в BankService есть пользователь с
             * данным логином и паролем (работайте только с теми пользователями что есть).
             */
            String login = input.askStr("Ваш логин: ");
            String password = input.askStr("Ваш password: ");
        }
        return rsl;
    }

    /**
     * В этом методе обращайтесь к банковскому сервису, печатайте баланс.
     *
     * @param requisite Строка в произвольной форме, используется для поиска получателя перевода.
     */
    public static void showBalance(BankService bankService, Input input, String requisite) {
        bankService.balance(requisite);
    }

    /**
     * В этом методе обращайтесь к банковскому сервису, уточняйте у пользователя на сколько он хочет
     * пополнить баланс, каким способом... печатайте результат, может быть баланс после пополнения
     * (на ваше усмотрение).
     *
     * @param requisite Строка в произвольной форме, используется для поиска получателя перевода.
     */
    public static void topUpBalance(BankService bankService, Input input, String requisite) {

    }

    /**
     * Перевести средства - также общаетесь в этом методе с пользователем и передаёте информацию,
     * так как операция важная желательно ещё раз заставлять вводить пароль/логин и передавать информацию
     * в BankService. Exceptions пользователю печатать не надо (как и в других методах этого класса),
     * вводите подсказки или написанные вами сообщения об ошибках.
     *
     * @param requisite Строка в произвольной форме, используется для поиска получателя перевода.
     */
    public static void transferTo(BankService bankService, Input input, String requisite) {

    }

    /**
     * Печатается меню пользователя (только печатается, общения с пользователем нету).
     */
    public static void showMenu() {
        System.out.println("0. Показать баланс");
        System.out.println("1. Пополнить баланс");
        System.out.println("2. Перевести средства");
        System.out.println("3. Выйти из программы");
    }


    public static void main(String[] args) {
        BankService bankService = new BankService();
        // здесь создадите несколько аккаунтов на проверку
        // данные осмысленно заполните, не просто пустые строки
        bankService.addAccount(new BankAccount("", "", ""));
        // Ещё аккаунты

        // Наши действия мы будет хранить в специальном листе, вызывать из этого же списка
        List<TerConsumer<BankService, Input, String>> actions = List.of(
                StartUI::showBalance,
                StartUI::topUpBalance,
                StartUI::transferTo
        );
        // Наш Input можно менять на нужную реализацию (ValidateInput доделайте)
        Input input = new ValidateInput();
        // Запускаем наш UI передавая аргументами банковский сервис, экшены и Input.
        new StartUI(bankService, actions, input).init();
    }
}

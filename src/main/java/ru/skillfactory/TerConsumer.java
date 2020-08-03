package ru.skillfactory;

/**
 * Наш собственный интерфейс Потребителя с 3 параметрами.
 *
 * @param <T> the first input argument
 * @param <U> the second input argument
 * @param <V> the third input argument
 */
@FunctionalInterface
public interface TerConsumer<T, U, V> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param v the third input argument
     */
    void accept(T t, U u, V v);
}

package com.epam.andrii_loievets.concurrency.circular_buffer;

/**
 *
 * @author Andrii_Loievets
 * @version 1.0 26-March-2014
 * @param <E> type of the stored items
 */
public interface Buffer<E> {
    void put(E item);
    E get();
    boolean isEmpty();
    boolean isFull();
}

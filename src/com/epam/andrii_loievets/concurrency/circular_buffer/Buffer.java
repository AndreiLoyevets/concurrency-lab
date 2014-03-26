package com.epam.andrii_loievets.concurrency.circular_buffer;

/**
 *
 * @author Andrii_Loievets
 * @version 1.0 26-March-2014
 * @param <E> type of the stored items
 */
public interface Buffer<E> {
    
    /**
     * Puts the item to the buffer. If the buffer is full, waits.
     * @param item the item to be putted
     */
    void put(E item);
    
    /**
     * Pops the item from the buffer. If the buffer is empty, waits.
     * @return the item from the buffer
     */
    E get();
    
    /**
     * Checks whether the buffer is empty.
     * @return true if the buffer is empty, false - otherwise
     */
    boolean isEmpty();
    
    /**
     * Checks whether the buffer is full.
     * @return  true if the buffer is full, false - otherwise
     */
    boolean isFull();
}

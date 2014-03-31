package com.epam.andrii_loievets.concurrency.circular_buffer;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementation of ring buffer of integers using concurrent java library.
 *
 * @author Andrii_Loievets
 * @version 1.0 27-March-2014
 */
public class ConcurrentRingBuffer implements Buffer<Integer> {

    private final Integer[] buffer;
    private int oldest; // index of the oldest element
    private int newest; // index of the newest (latest added) element
    private final Lock lock;
    private final Condition notFull;
    private final Condition notEmpty;

    public ConcurrentRingBuffer(int capacity) {
        buffer = new Integer[capacity];
        lock = new ReentrantLock();
        notFull = lock.newCondition();
        notEmpty = lock.newCondition();
        oldest = 0; // as soon as we put something, it gets into buffer[0]
        newest = -1; // when we put something new, we first increment newest
    }

    @Override
    public void put(Integer item) {
        lock.lock();

        try {
            while (isFull()) {
                notFull.await();
            }

            // put item
            int pos = (newest + 1) % buffer.length;
            buffer[pos] = item;
            newest = pos;
            System.out.println(item + " added to buffer");

            notEmpty.signalAll();

        } catch (InterruptedException ex) {
            System.out.println(Thread.currentThread() + "interrupted when putting"
                    + item);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Integer get() {
        lock.lock();

        try {
            while (isEmpty()) {
                notEmpty.await();
            }

            // take item
            Integer item = buffer[oldest];
            buffer[oldest] = null;
            oldest = (1 + oldest) % buffer.length;

            notFull.signalAll();

            return item;
        } catch (InterruptedException ex) {
            System.out.println(Thread.currentThread() + "interrupted when getting item");
            return null;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < buffer.length; ++i) {
            if (buffer[i] != null) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isFull() {
        for (int i = 0; i < buffer.length; ++i) {
            if (buffer[i] == null) {
                return false;
            }
        }

        return true;
    }
}

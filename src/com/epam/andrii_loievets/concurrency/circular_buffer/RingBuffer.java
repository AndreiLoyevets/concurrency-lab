package com.epam.andrii_loievets.concurrency.circular_buffer;

/**
 * Implementation of ring buffer using synchronized constructions without
 * java.util.concurrent.
 * 
 * @author Andrii_Loievets
 * @version 1.0 26-March-2014
 */
public class RingBuffer implements Buffer<Integer> {
    private final Integer [] buffer;
    private int oldest; // index of the oldest element
    private int newest; // index of the newest (latest added) element
    
    public RingBuffer(int capacity) {
        buffer = new Integer[capacity]; 
        oldest = 0; // as soon as we put something, it gets into buffer[0]
        newest = -1; // when we put something new, we first increment newest
    }

    @Override
    public synchronized void put(Integer item) {
        while (isFull()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println("Interrupted exception in thread "
                + Thread.currentThread() + " while putting " + item);
                return;
            }
        }
        
        // calculate next free position
        int pos = (newest + 1) % buffer.length;
        buffer[pos] = item;
        newest = pos;
        
        System.out.println(item + " added to buffer");
        
        notifyAll();
    }

    @Override
    public synchronized Integer get() {
        while (isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println("Interrupted exception in thread "
                + Thread.currentThread() + " while getting an item");
                return null;
            }
        }
        
        // here the buffer is not empty, get oldest item
        Integer item = buffer[oldest];
        buffer[oldest] = null;
        oldest = (1 + oldest) % buffer.length;
        
        notifyAll();
        
        return item;
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
    
    private void printBufferState() {
        for (int i = 0; i < buffer.length; ++i) {
            System.out.print(buffer[i] + " ");
        }
        System.out.println();
    }
}

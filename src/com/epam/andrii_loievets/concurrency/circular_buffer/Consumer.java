package com.epam.andrii_loievets.concurrency.circular_buffer;

/**
 * This class represents a consumer for producer-consumer problem.
 * 
 * @author Andrii_Loievets
 * @version 1.0 26-March-2014
 */
public class Consumer implements Runnable {
    private Buffer<Integer> buffer;
    
    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Consumer gets " + buffer.get());
        }
    }
}

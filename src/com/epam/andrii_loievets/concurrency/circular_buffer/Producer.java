package com.epam.andrii_loievets.concurrency.circular_buffer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrii_Loievets
 * @version 1.0 26-March-2014
 */
public class Producer implements Runnable {
    private final Buffer buffer;
    private volatile static int i;
    
    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                System.out.println("Producer interrupted");
                return;
            }
            
            buffer.put(i++);
            System.out.println("Producer puts " + i);
        }
    }
    
}

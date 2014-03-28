package com.epam.andrii_loievets.concurrency.circular_buffer;

/**
 *
 * @author Andrii_Loievets
 * @version 1.0 26-March-2014
 */
public class TestProducerConsumer {
    public static void noConcurrentTest() {
        final int BUFFER_SIZE = 10;
        final int NUM_PRODUCERS = 5;
        final int NUM_CONSUMERS = 3;
        
        Buffer buffer = new RingBuffer(BUFFER_SIZE);
        
        for (int i = 0; i < NUM_PRODUCERS; ++i) {
            new Thread(new Producer(buffer)).start();
        }
        
        for (int i = 0; i < NUM_CONSUMERS; ++i) {
            new Thread(new Consumer(buffer)).start();
        }
    }
    
    public static void concurrentTest() {
        final int BUFFER_SIZE = 10;
        final int NUM_PRODUCERS = 5;
        final int NUM_CONSUMERS = 3;
        
        Buffer buffer = new ConcurrentRingBuffer(BUFFER_SIZE);
        
        for (int i = 0; i < NUM_PRODUCERS; ++i) {
            new Thread(new Producer(buffer)).start();
        }
        
        for (int i = 0; i < NUM_CONSUMERS; ++i) {
            new Thread(new Consumer(buffer)).start();
        }
    }
    
    public static void main(String [] args) {
        concurrentTest();
    }
}

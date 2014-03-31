package com.epam.andrii_loievets.concurrency.circular_buffer;

import java.util.Scanner;

/**
 * Demonstration of producer-consumer problem with 2 different implementations:
 * 1) with synchronized-blocks and 2) locks.
 *
 * @author Andrii_Loievets
 * @version 1.0 26-March-2014
 */
public class ProducerConsumerDemo {

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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choise;

        System.out.println("Enter 1 to run test with java.util.concurrent");
        System.out.println("Enter 2 to run test without java.util.concurrent");

        choise = scanner.nextInt();

        switch (choise) {
            case 1:
                concurrentTest();
                break;
            case 2:
                noConcurrentTest();
                break;
            default:
                System.out.println("Wrong option!");
                break;
        }
    }
}

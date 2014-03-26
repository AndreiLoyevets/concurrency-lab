package com.epam.andrii_loievets.concurrency;

import java.util.Random;

public class MergeSort implements Runnable {
    private final int[] array; // shared between threads but the same
    private final int startPos;
    private final int endPos;
    
    public MergeSort(int [] array, int start, int end) {
        this.array = array;
        this.startPos = start;
        this.endPos = end;
    }

    public void sort(int start, int end) {
        if (end - start <= 1) {
            return;
        }

        int middle = (start + end) / 2;

        Thread th = new Thread(new MergeSort(array, start, middle));
        th.start();
        sort(middle, end);
        
        try {
            th.join();
        } catch (InterruptedException ex) {
            System.out.println(Thread.currentThread() + " interrupted unexpectedly");
            return;
        }

        merge(start, middle, end);
    }

    public void merge(int start, int middle, int end) {
        int posLeft = start, posRight = middle;

        while (posLeft != posRight && posRight < end) {
            if (array[posLeft] > array[posRight]) {
                int temp = array[posRight];
                System.arraycopy(array, posLeft, array, posLeft + 1, posRight
                        - posLeft);
                array[posLeft] = temp;
                ++posRight;
            }
            ++posLeft;
        }
    }
    
    @Override
    public void run() {
        sort(startPos, endPos);
    }

    public static int [] getRandomArray(int size) {

        int [] list = new int[size];

        Random random = new Random(System.currentTimeMillis());

        for (int i = 0; i < size; ++i) {
            list[i] = random.nextInt(100);
        }

        return list;
    }
    
    public void printArray() {
        for (int i : array) {
            System.out.println(i + " ");
        }
    }

    public static void main(String [] args) {
        final int ARRAY_SIZE = 100;
        MergeSort mSort = new MergeSort(MergeSort.getRandomArray(ARRAY_SIZE), 0,
                ARRAY_SIZE);
        
        System.out.println("Before sort:");
        mSort.printArray();
        mSort.sort(0, mSort.array.length);
        System.out.println("\nAfter sort:");
        mSort.printArray();
    }
}

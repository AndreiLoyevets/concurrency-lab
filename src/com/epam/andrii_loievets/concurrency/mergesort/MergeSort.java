package com.epam.andrii_loievets.concurrency.mergesort;

import java.util.Random;

/**
 * This class represents a concurrent merge sort algorithm.
 * To sort an integer array, create an instance of MergeSort, pass the array and
 * range (start inclusive, end - exclusive).
 * 
 * @author Andrii_Loievets
 * @version 2.0 26-March-2014
 */
public class MergeSort implements Runnable {
    private final int[] array; // shared between threads
    private final int startPos;
    private final int endPos;
    
    /**
     * @param array array to be sorted
     * @param start start position in the array
     * @param end  position after the end of the array
     */
    public MergeSort(int [] array, int start, int end) {
        this.array = array;
        this.startPos = start;
        this.endPos = end;
    }

    /**
     * Sorts the range [start, end) of the array.
     * @param start start position
     * @param end  position after the end of the range
     */
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

    /**
     * Merges to consecutive parts of the array.
     * @param start start of the left part
     * @param middle start of the right part
     * @param end position after the end of the right part
     */
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

    /**
     * Generates random array of integers of a given size.
     * @param size size of the array to be generated
     * @param range range for generated values
     * @return array filled with pseudo random integers
     */
    public static int [] getRandomArray(int size, int range) {

        int [] list = new int[size];

        Random random = new Random(System.currentTimeMillis());

        for (int i = 0; i < size; ++i) {
            list[i] = random.nextInt(range);
        }

        return list;
    }
    
    /**
     * Prints the stored array to the console.
     */
    public void printArray() {
        for (int i : array) {
            System.out.println(i + " ");
        }
    }

    /**
     * Generates random array, sorts it and display the before-after results.
     * @param args 
     */
    public static void main(String [] args) {
        final int ARRAY_SIZE = 100;
        MergeSort mSort = new MergeSort(MergeSort.getRandomArray(ARRAY_SIZE,100),
                0, ARRAY_SIZE);
        
        System.out.println("Before sort:");
        mSort.printArray();
        mSort.sort(0, mSort.array.length);
        System.out.println("\nAfter sort:");
        mSort.printArray();
    }
}

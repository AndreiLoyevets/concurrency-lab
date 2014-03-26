package com.epam.andrii_loievets.concurrency;

import java.util.Random;

public class MergeSort {
    private int[] array;

    public void sort(int start, int end) {
        if (end - start <= 1) {
            return;
        }

        int middle = (start + end) / 2;

        sort(start, middle);
        sort(middle, end);

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

    public int [] getRandomArray(int size) {

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
        MergeSort mSort = new MergeSort();
        
        mSort.array = mSort.getRandomArray(10);
        System.out.println("Before sort:");
        mSort.printArray();
        mSort.sort(0, mSort.array.length);
        System.out.println("\nAfter sort:");
        mSort.printArray();
    }
}

package com.epam.andrii_loievets.concurrency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Andrii_Loievets
 * @version 1.0 24-March-2014
 */
public class MergeSort implements Callable<Object> {
    private static List<Integer> list;
    private int start;
    private int end;
    
    public static void init(List<Integer> sharedList) {
        if (sharedList == null) {
            throw new NullPointerException("Cannot init the MergeSort with null list");
        } else {
            list = sharedList;
        }
    }
    
    /**
     * Set ranges for sorting sublist.
     * @param start start position (inclusive)
     * @param end end position (exclusive) 
     */
    private void setRanges(int start, int end) {
        if (start >= end) {
            throw new IllegalArgumentException("Invalid range [" + start + ", "
            + end + ")");
        }
        
        this.start = start;
        this.end = end;
    }
  
    public static void merge(int start, int middle, int end) {
        int posLeft = start, posRight = middle;
        
        while (posLeft != posRight) {
            if (list.get(posLeft) > list.get(posRight)) {
                int temp = list.get(posRight);
                System.arraycopy(list, posLeft, list, posLeft + 1, posRight - posLeft);
                list.set(posLeft, temp);
                
                ++posRight;
            }
            ++posLeft;
        }
    }
    
    public List<Integer> getRandomArray(int size) {
        List<Integer> list = new ArrayList<>(size);
        Random random = new Random(System.currentTimeMillis());
        
        for (int i = 0; i < size; ++i) {
            list.add(random.nextInt(100));
        }
        
        return list;
    }
    
    public void printList(List<Integer> list) {
        for (int i : list) {
            System.out.print(i + " ");
        }
    }
    
    public static void main(String [] args) {
        final int ARRAY_SIZE = 10;
        MergeSort mSort = new MergeSort();
        List<Integer> list = mSort.getRandomArray(ARRAY_SIZE);
        
        System.out.println("Before sort:");
        mSort.printList(list);
        System.out.println("\nAfter sort:");
        MergeSort.init(list);
        mSort.setRanges(0, ARRAY_SIZE);
        
        try {
            mSort.call();
        } catch (Exception ex) {
            Logger.getLogger(MergeSort.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Object call() throws Exception {
        if (end - start <= 1) {
            return new Object();
        }
        
        int middle = (start + end) / 2;
        
        ExecutorService execService = Executors.newFixedThreadPool(2);
        Collection<MergeSort> subsorts = new ArrayList<>(2);
        
        MergeSort mergeSort = new MergeSort();
        mergeSort.setRanges(start, middle);
        subsorts.add(mergeSort);
        
        mergeSort = new MergeSort();
        mergeSort.setRanges(middle, end);
        subsorts.add(mergeSort);
        
        List<Future<Object>> results = execService.invokeAll(subsorts);
        
        // wait until all threads finished
        for (Future<Object> future : results) {
            future.get();
        }
        
        // merge results
        merge(start, middle, end);
        
        return new Object();
    }
}

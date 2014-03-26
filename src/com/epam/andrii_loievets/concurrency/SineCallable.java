package com.epam.andrii_loievets.concurrency;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * This class calculates the sum of sines in the form:
 * sin(startValue) + sin(startValue + step) + ... + sin(endValue)
 * Method call() returns the value of this sum.
 * 'step' value is common for all SineCallable instances and is defined by
 * the number of threads.
 * 
 * @author Andrii_Loievets
 * @version 1.0 24-March-2014
 */
public class SineCallable implements Callable<Double>{
    private static int step;
    private int startValue;
    private int endValue;

    @Override
    public Double call() throws Exception {        
        double result = 0.0;
        int arg = 0;
        
        for (arg = startValue; arg <= endValue; arg += step) {
            result += Math.sin(arg);
        }
        
        return result;
    }
    
    public static Collection<SineCallable> init(int numThreads, int upperBound) {
        if (upperBound < 0) {
            throw new IllegalArgumentException("Upper bound (N) cannot be negative");
        }
        
        if (numThreads < 1) {
            throw new IllegalArgumentException("Cannot configure " + numThreads
                    + " threads");
        }
        
        List<SineCallable> callables = new ArrayList<>(numThreads);
        int start = -upperBound;
        
        step = numThreads;
        
        for (int i = 0; i < numThreads; ++i) {
            SineCallable callable = new SineCallable();
            callable.startValue = start;
            callable.endValue = upperBound;
            callables.add(callable);
            ++start;
        }
        
        return callables;
    }
    
    public static double runTest(int numThreads, int N) {
        ExecutorService execService = Executors.newFixedThreadPool(numThreads);
        List<Future<Double>> results = null;
        
        try {
            results = execService.invokeAll(init(numThreads, N));
        } catch (InterruptedException ex) {
            System.out.println("Unexpected interruption of ExecutorService");
            return -1.0;
        } finally {
            execService.shutdown();
        }
        
        double sum = 0.0;
        int counter = 0;
        
        for (Future<Double> future : results) {
            try {
                sum += future.get();
            } catch (InterruptedException ex) {
                System.out.println("Interrupted while getting result from thread "
                + counter);
                return -1.0;
            } catch (ExecutionException ex) {
                System.out.println("ExecutionException while getting result from"
                        + "thread" + counter);
            }
            ++counter;
        }
        
        return sum;
        
        
    }
    
    public static void main(String [] args) {
        final int NUM_THREADS = 2;
        final int N = 1000;
        long startTime, endTime;
        
        startTime = System.currentTimeMillis();
        double sum = runTest(NUM_THREADS, N);
        endTime = System.currentTimeMillis();
        
        System.out.println("sin(" + (-N) + ") + ... + sin(" + N + ") = " + sum);
        System.out.println("Time spent: " + (endTime - startTime) + "ms");
    }
}
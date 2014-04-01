package com.epam.andrii_loievets.concurrency.geom_progression;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrii_Loievets
 * @version 1.0 31-March-2014
 */
public class ConcGeomProgressionTest {
    
    private static ConcurrentHashMap<BigInteger, Object> results;
    
    public ConcGeomProgressionTest() {
    }
    
    @Before
    public void setUp() {
        results = new ConcurrentHashMap<>(1000);
    }
    
    @After
    public void tearDown() {
        results = null;
    }
    
    @Test
    public void next_2Threads10Numbers_NoDuplicatesAndMissingVal() {
        ExecutorService es = Executors.newFixedThreadPool(2);
        ConcGeomProgression cgp = new ConcGeomProgression();
        
        GeomProber.setCounter(10);
        
        for (int i = 0; i < 2; ++i) {
            es.submit(new GeomProber(cgp));
        }
        
        es.shutdown();
        
        for (int i = 1; i <= results.size(); ++i) {

            // check no values were skipped
            assertTrue("Value " + i + " is missing",
                    results.containsKey(new BigInteger(Integer.toString(i))));
        }
    }
    
    @Test
    public void next_100Threads1000Numbers_NoDuplicatesAndMissingVal() {
        ExecutorService es = Executors.newFixedThreadPool(100);
        ConcGeomProgression cgp = new ConcGeomProgression();
        
        GeomProber.setCounter(1000);
        
        for (int i = 0; i < 100; ++i) {
            es.submit(new GeomProber(cgp));
        }
        
        es.shutdown();
        
        for (int i = 1; i <= results.size(); ++i) {

            // check no values were skipped
            assertTrue("Value " + i + " is missing",
                    results.containsKey(new BigInteger(Integer.toString(i))));
        }
    }
    
    @Test
    public void next_2000Threads10000Numbers_NoDuplicatesAndMissingVal() {
        ExecutorService es = Executors.newFixedThreadPool(2000);
        ConcGeomProgression cgp = new ConcGeomProgression();
        
        GeomProber.setCounter(10000);
        
        for (int i = 0; i < 2000; ++i) {
            es.submit(new GeomProber(cgp));
        }
        
        es.shutdown();
        
        for (int i = 1; i <= results.size(); ++i) {

            // check no values were skipped
            assertTrue("Value " + i + " is missing",
                    results.containsKey(new BigInteger(Integer.toString(i))));
        }
    }
    
    static class GeomProber implements Runnable {
        
        private static volatile int counter;
        private final ConcGeomProgression cgp;
        
        public static void setCounter(int numElements) {
            counter = numElements;
        }
        
        public GeomProber(ConcGeomProgression cgp) {
            this.cgp = cgp;
        }
        
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted() && counter > 0) {
                assertNull("Duplicate found!", results.put(cgp.next(), null));
                --counter;
            }
        }
    }
}

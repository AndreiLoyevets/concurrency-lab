package com.epam.andrii_loievets.concurrency.geom_progression;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class allows safe generation of the geometric progression 1, 2, 4, ...
 * in different threads.
 * 
 * @author Andrii_Loievets
 * @version 1.0 31-3-2014
 */
public class ConcGeomProgression {
    private final AtomicReference<BigInteger> nextNum
            = new AtomicReference<>(BigInteger.ONE);;
    
    public BigInteger next() {
        return nextNum.getAndSet(nextNum.get().add(nextNum.get()));
    }
}

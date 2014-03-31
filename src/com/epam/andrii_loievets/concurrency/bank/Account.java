package com.epam.andrii_loievets.concurrency.bank;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class represents simple bank account which allows to withdraw and
 * deposit money, check the amount.
 * 
 * @author Andrii_Loievets
 * @version 1.0 30-March-2014
 */
public class Account {

    private final int id;
    private int amount;
    private final Lock lock = new ReentrantLock();

    public Account(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public Account(int id) {
        this(id, 0);
    }

    public void withdraw(int amount) {
        this.amount -= amount;
    }

    public void deposit(int amount) {
        this.amount += amount;
    }

    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public Lock getLock() {
        return lock;
    }
}

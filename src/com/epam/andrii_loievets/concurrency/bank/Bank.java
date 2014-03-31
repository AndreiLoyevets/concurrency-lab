package com.epam.andrii_loievets.concurrency.bank;

import java.util.concurrent.TimeUnit;

/**
 * This class contains a collection of accounts and allows a thread-safe
 * transferring money from one account to another.
 * 
 * @author Andrii_Loievets
 * @version 2.0 30-March-2014
 */
public class Bank {

    private final Account[] accounts;
    private int totalAmount;

    public Bank(Account[] accounts) {
        this.accounts = accounts;
    }

    public void transfer(Account from, Account to, int amount) {
        boolean fromLock = false;
        boolean toLock = false;

        try {
            while (!fromLock || !toLock) {
                fromLock = from.getLock().tryLock(
                        (long) (Math.random() * 1000), TimeUnit.MILLISECONDS);
                toLock = to.getLock().tryLock((long) (Math.random() * 1000),
                        TimeUnit.MILLISECONDS);
            }
            from.withdraw(amount);
            to.deposit(amount);
        } catch (InterruptedException e) {
            System.out.println("Transfer from account #" + from.getId()
             + " to account #" + to.getId() + " was interrupted");
        } finally {
            from.getLock().unlock();
            to.getLock().unlock();
        }
    }

    public Account getAccount(int index) {
        if (index < 0 || index >= accounts.length) {
            throw new IllegalArgumentException("Cannot retrieve account #"
                    + index);
        }

        return accounts[index];
    }

    public int calcTotalAmount() {
        totalAmount = 0;

        for (Account account : accounts) {
            totalAmount += account.getAmount();
        }
        return totalAmount;
    }

    public int getTotalAmount() {
        return totalAmount;
    }
}

package com.epam.andrii_loievets.concurrency.bank;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class demonstrates running of a large number of transfers in
 * different threads. It compares total amount before and after the transfers.
 *
 * @author Andrii_Loievets
 * @version 2.0 31-March-2014
 */
public class BankDemo {

    private final int NUM_ACCOUNTS = 100;
    private final int MAX_INIT_AMOUNT = 1000;
    private final int NUM_THREADS = 1000;
    private final int MAX_TRANSFER = 100;
    private Bank bank;
    private final AtomicInteger numTransactions;

    public BankDemo() {
        numTransactions = new AtomicInteger(0);
    }

    public void initBank() {
        Account[] accounts = new Account[NUM_ACCOUNTS];

        for (int i = 0; i < NUM_ACCOUNTS; ++i) {
            Account account = new Account(i,
                    (int) (Math.random() * MAX_INIT_AMOUNT));
            accounts[i] = account;
        }

        bank = new Bank(accounts);
        bank.calcTotalAmount();
    }

    public Account[] getRandomPair() {
        Account[] fromToAccounts = new Account[2];
        int[] index = new int[2];

        while ((fromToAccounts[0] == null) || (fromToAccounts[1] == null)) {
            try {
                index[0] = (int) (Math.random() * MAX_INIT_AMOUNT);
                index[1] = (int) (Math.random() * MAX_INIT_AMOUNT);

                if (index[0] != index[1]) {
                    fromToAccounts[0] = bank.getAccount(index[0]);
                    fromToAccounts[1] = bank.getAccount(index[1]);
                }

            } catch (IllegalArgumentException e) {
                fromToAccounts[0] = null;
                fromToAccounts[1] = null;
            }
        }

        return fromToAccounts;
    }

    public void runThreads() {
        ExecutorService es = Executors.newFixedThreadPool(10);
        List<Future<?>> futures = new ArrayList<>(NUM_THREADS);

        System.out.println("Amount before: " + bank.getTotalAmount());

        for (int i = 0; i < NUM_THREADS; ++i) {
            Runnable task;
            task = new Runnable() {
                private final Account[] transferAccounts = getRandomPair();

                @Override
                public void run() {
                    bank.transfer(transferAccounts[0], transferAccounts[1],
                            (int) (Math.random() * MAX_TRANSFER));

                    numTransactions.incrementAndGet();
                }

            };

            futures.add(es.submit(task));
        }

        // wait for result
        for (int i = 0; i < NUM_THREADS; ++i) {
            try {
                futures.get(i).get();
            } catch (InterruptedException e) {
                System.out.println("Transfer interrupted");
                es.shutdown();
                return;
            } catch (ExecutionException e) {
                System.out.println("Transfer execution interrupted");
                es.shutdown();
                return;
            }
        }

        System.out.println("Amount after: " + bank.calcTotalAmount());
        System.out.println("Number of transactions: " + numTransactions);
        System.out.println("Number of threads " + NUM_THREADS);
        es.shutdown();
    }

    public static void main(String[] args) {
        BankDemo tb = new BankDemo();

        tb.initBank();
        tb.runThreads();
    }
}

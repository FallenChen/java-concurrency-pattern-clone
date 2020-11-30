package com.garry.features.locks;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Special lock with a different strategy: allow multiple readers simultaneously
 * and only one single writer
 *
 * ReadLock readLock = rwLock.readLock();
 *
 * WriteLock writeLock = rwLock.writeLock();
 *
 * Read access is granted if there is no Writer or a Writer requesting access.
 *
 * Writer access is granted if there is no Reader
 *
 * ReentrantReadWriterLock gives reentrant capabilities do ReadWriteLock
 *
 * Fair in constructor:
 *
 * true: Fair lock, newly requesting threads are queue if the lock is held
 *
 * false: Unfair lock: if the lock is held, requesting threads can 'jump' the
 * waiting queue (default, specially for a write lock).
 */
public class UsingExplicitReadWriteLocks {

    // Equivalent to Intrinsic Locks
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private String myContent = "A long default content...";

    /**
     * Simplest way to use the read mode
     */
    public String showContent(){
        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        readLock.lock();
        try {
            System.out.println("Reading state while holding a lock.");
            return myContent;
        }finally {
            readLock.unlock();
        }
    }

    public void writeContent(String newContentToAppend){
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
        writeLock.lock();
        try {
            System.out.println("Writing " + newContentToAppend);
            myContent = new StringBuilder().append(myContent).append(newContentToAppend).toString();
        }finally {
            writeLock.lock();
        }
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        UsingExplicitReadWriteLocks self = new UsingExplicitReadWriteLocks();

        // Readers
        for (int i = 0; i < 10; i++) {
            executor.submit(() ->{
                // Delay readers to start
                try {
                    Thread.sleep(new Random().nextInt(10) * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(self.showContent());
            });

        }

        // Writers - only if no writer is available
        for (int i = 0; i < 5; i++) {
            executor.execute(() -> self.writeContent(UUID.randomUUID().toString()));

        }
        executor.shutdown();
    }

}

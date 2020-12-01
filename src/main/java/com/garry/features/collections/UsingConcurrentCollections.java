package com.garry.features.collections;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * Concurrent collections are an alternative to the Synchronized Collections
 * <p>
 * Support concurrent access from n threads and performs better than Synchronized collections
 */
public class UsingConcurrentCollections {

    /**
     * Map for Concurrent access.Sacrifices some aspects of synchronization to
     * achieve better performance
     * <p>
     * Locks it's values with Lock Striping
     * <p>
     * Lock Striping divides the protected region through several locks
     * <p>
     * - Don't throw ConcurrentModificationException
     * - size() and isEmpty() can be incorrect. Don't rely on them
     * - Supports atomic operations, don't need client side locking
     * - Readers can access concurrently, and iterator have weak consistency
     * - Just a few Writers can modify it
     */

    public static void usingConcurrentHashMap() {
        System.out.println("=== ConcurrentHashMap ===");
        ExecutorService executor = Executors.newCachedThreadPool();
        Random random = new Random();
        ConcurrentHashMap<UUID, Integer> valuesPerUuid = new ConcurrentHashMap<>();
        // atomic operations
        valuesPerUuid.putIfAbsent(UUID.randomUUID(), random.nextInt(100));

        // simulating concurrent access - no duplicates should be inserted
        for (int i = 0; i < 100; i++) {
            if (i % 6 == 0) {
                // write
                executor.execute(() -> {
                    UUID uuid = UUID.randomUUID();
                    Integer value = random.nextInt(10);
                    System.out.println("Added " + uuid + " - " + value);
                    valuesPerUuid.putIfAbsent(uuid, value);
                });
            } else {
                // read
                executor.execute(() -> System.out.println("Printed " + valuesPerUuid.values().toString()));
            }

        }

        // Finishing
        executor.shutdown();
        try {
            executor.awaitTermination(2000, TimeUnit.SECONDS);
            // space for other examples
            Thread.sleep(2000);
            System.out.println("\n\n\n\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Replacement for synchronized list.Based on the immutable object concept
     * <p>
     * Use when reading is far more common than writing
     * <p>
     * Creates a new copy every time that the list is modified, only synchronizing briefly to ensure array content visibility
     * <p>
     * iterator returns a snapshot of the current state of the collection
     * <p>
     * Supports atomic operations
     */
    public static void usingCopyOnWriteArrayList() {
        System.out.println("=== CopyOnWriteArrayList ===");
        ExecutorService executor = Executors.newCachedThreadPool();
        Random random = new Random();
        // No ConcurrentModificationException
        CopyOnWriteArrayList<Integer> copyOnWriteArrayList = new CopyOnWriteArrayList<>();

        for (int i = 0; i < 100; i++) {
            if (i % 8 == 0) {
                //write
                executor.execute(() -> {
                    int value = random.nextInt(10);
                    System.out.println("Added " + value);
                    copyOnWriteArrayList.add(value);
                });
            } else {
                // read
                executor.execute(() -> {
                    StringBuilder builder = new StringBuilder();
                    for (Integer value : copyOnWriteArrayList) {
                        builder.append(value + " ");
                    }
                    System.out.println("Reading " + builder.toString());
                });
            }
        }
        // Finishing
        executor.shutdown();
        try {

            executor.awaitTermination(2000, TimeUnit.SECONDS);
            // space for other examples
            Thread.sleep(2000);
            System.out.println("\n\n\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Concurrent Queue interface
     * <p>
     * Implementations : LinkedBlockingQueue, ArrayBlockingQueue,
     * PriorityBlockingQueue, SynchronizedQueue
     * <p>
     * Used for the Producer-Consumer pattern
     * <p>
     * Blocking methods: put/take; Timed blocking methods: offer, poll
     * <p>
     * Can be bounded or unbounded
     */

    public static void usingBlockingQueue() {
        System.out.println("=== BlockingQueue ===");

        // Bounded UUID queue
        BlockingDeque<UUID> uuidQueue = new LinkedBlockingDeque<UUID>(10);

        // Multiple consumers
        Runnable runConsumer = () -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {

                    UUID uuid = uuidQueue.take();
                    System.out.println("Consumed: " + uuid + " by " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    // interrupted pattern
                    // InterruptedException makes isInterrupted returns false
                    Thread.currentThread().interrupt();
                    System.out.println("Consumer Finished");
                }
            }
        };
        Thread consumer1 = new Thread(runConsumer);
        consumer1.start();
        Thread consumer2 = new Thread(runConsumer);
        consumer2.start();

        Runnable runProducer = () -> {
            try {

                while (!Thread.currentThread().isInterrupted()) {
                    Random random = new Random();
                    // Delay producer
                    Thread.sleep(random.nextInt(1000));
                    UUID randomUUID = UUID.randomUUID();
                    System.out.println("Produced: " + randomUUID + " by " + Thread.currentThread().getName());
                    uuidQueue.put(randomUUID);
                }
            }catch (InterruptedException e){
                // interrupted pattern
                System.out.println("Producer Finished");
            }
        };

        // Multiple producers - Examples using simple threads this time
        Thread producer1 = new Thread(runProducer);
        producer1.start();
        Thread producer2 = new Thread(runProducer);
        producer2.start();
        Thread producer3 = new Thread(runProducer);
        producer3.start();

        try {
            // Queue will run for 10ses
            Thread.sleep(10000);
            producer1.interrupt();
            producer2.interrupt();
            producer3.interrupt();
            consumer1.interrupt();
            consumer2.interrupt();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
//        usingConcurrentHashMap();
//        usingCopyOnWriteArrayList();
        usingBlockingQueue();
    }

}

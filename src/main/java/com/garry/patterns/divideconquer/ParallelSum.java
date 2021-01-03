package com.garry.patterns.divideconquer;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * Pattern: Parallel Divide and Conquer
 *
 * Example: Parallel Sum
 *
 * This is a simple example for educational  purpose only
 */
public class ParallelSum extends RecursiveTask<BigInteger> {

    private final static int THRESHOLD = 10_000; // Choosing a number to split the conputation

    private List<BigInteger> nums;

    public ParallelSum(List<BigInteger> nums) {
        this.nums = nums;
    }

    @Override
    protected BigInteger compute() {
        int size = nums.size();
        if (size < THRESHOLD)
        {
            return sequentialSum(nums);
        }else {
            ParallelSum x = new ParallelSum(nums.subList(0, size / 2));
            ParallelSum y = new ParallelSum(nums.subList(size/2, size));
            x.fork();
            y.fork();
            BigInteger xResult = x.join();
            BigInteger yResult = y.join();
            return yResult.add(xResult);

        }
    }
    private static BigInteger sequentialSum(List<BigInteger> nums)
    {
        BigInteger acc = BigInteger.ZERO;
        for(var value: nums)
        {
            acc = acc.add(value);
        }
        return acc;
    }

    public static void main(String[] args) throws InterruptedException {
        List<BigInteger> nums = LongStream.range(0, 10_000_000L)
                .mapToObj(BigInteger::valueOf)
                .collect(Collectors.toList());

        Runnable parallel = () -> {
            var commonPool = ForkJoinPool.commonPool();
            var result = commonPool.invoke(new ParallelSum(nums));

            System.out.println("Parallel Result is: " + result);
        };

        Runnable sequential = () -> {
            var acc = sequentialSum(nums);

            System.out.println("Sequential Result is: " + acc);
        };

//        sequential.run();
        parallel.run();

        Thread.sleep(2000);

        System.out.println("#### After some JIT \n\n");

//        dummyBenchmark(sequential);
        dummyBenchmark(parallel);

        Thread.sleep(2000);

        System.out.println("#### After more JIT \n\n");

//        dummyBenchmark(sequential);
        dummyBenchmark(parallel);
    }

    static void getHot(Runnable runnable) {
        runnable.run();
    }

    static void dummyBenchmark(Runnable runnable) {
        var before = System.currentTimeMillis();
        runnable.run();
        var after = System.currentTimeMillis();
        System.out.println("Executed in: " + (after - before));
        System.out.println("######\n");
    }
}
package com.garry.patterns.task_execution;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Pattern: Background Task Executor
 *
 * Motivation:Executing tasks outside the main thread is useful when some task
 * may take a long time to run and you don't want to wait for it to end
 *
 * Intent: Creates a Background task executor with the ability to execute and cancel tasks
 *
 * Applicability: Sending an email after a user registration in your web-app;or
 * executing a long running task in background;use the Background Task Executor
 * to not compromise the main thread or the current thread you're using
 */
public class BackgroundTaskExecutor {

    public static interface OnInterruption<T> {
        void accept(Future<T> t,Exception exception);
    }

    public static interface OnShutdownError {
        void accept(ExecutorService executor, Exception exception);
    }

    private final ExecutorService executor;

    public BackgroundTaskExecutor(int threadsForTasks) {
        this.executor = Executors.newFixedThreadPool(threadsForTasks);
    }

    public <T> Future<T> execute(Callable<T> task){
        Future<T> submitted = executor.submit(task);
        return submitted;
    }

    public <T> List<Future<T>> execute(List<Callable<T>> tasks){
        List<Future<T>> futureTasks = tasks.stream()
                .map(executor::submit)
                .collect(Collectors.toList());
        return futureTasks;
    }

    public <T> boolean cancel(Future<T> task){
        boolean canceled = task.cancel(true);
        return canceled;
    }

    public <T> boolean cancel(List<Future<T>> task){
        boolean hasFalse = task.stream()
                .map(f -> f.cancel(true))
                .anyMatch(b -> b.equals(false));
        return !hasFalse;
    }

    public <T> List<Optional<T>> completeTask(List<Future<T>> tasks, OnInterruption<T> onInterruption){
        Function<Future<T>, Optional<T>> fn =  (task)->{
            try {
                return Optional.ofNullable(task.get());
            }catch (InterruptedException | ExecutionException e){
                onInterruption.accept(task,e);
                return Optional.empty();
            }
        };
        List<Optional<T>> results = tasks.stream()
                .map(fn)
                .collect(Collectors.toList());
        return results;
    }

    public <T> Optional<T> completeTask(Future<T> task, OnInterruption<T> onInterruption){
        try {
            return Optional.ofNullable(task.get());
        }catch (InterruptedException  | ExecutionException e){
            onInterruption.accept(task,e);
            return Optional.empty();
        }
    }

    public void shutdownTasks(long timeout,TimeUnit timeUnit, OnShutdownError onShutdownError){
        executor.shutdown();
        try {
            executor.awaitTermination(timeout,timeUnit);
        }catch (InterruptedException e){
            onShutdownError.accept(executor,e);
        }
    }

    public List<Runnable> shutdownNowTasks(long timeout, TimeUnit timeUnit,OnShutdownError onShutdownError){
        List<Runnable> remainingTasks = executor.shutdownNow();
        try {
            executor.awaitTermination(timeout,timeUnit);
        }catch (InterruptedException e)
        {
            onShutdownError.accept(executor,e);
        }
        return remainingTasks;
    }


}

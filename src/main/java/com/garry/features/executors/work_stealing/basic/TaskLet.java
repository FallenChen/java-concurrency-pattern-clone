package com.garry.features.executors.work_stealing.basic;

import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.RecursiveAction;

/**
 * recursively perform a task
 */
abstract class TaskLet extends RecursiveAction {

    public final HashSet<TaskLet> master;

    protected ConcurrentLinkedDeque<TaskLet> originDeque;

    // Master object keeps set of TaskLets
    // TaskLet is removed from its master when it completes
    // If master is empty at that point, notify() is called
    // master() should only be manipulated in Scheduler or in one of
    // its Server threads, and only in a synchronized block
    public TaskLet(HashSet<TaskLet> master) {
        this.master = master;
    }

    abstract public boolean isLeaf();

    abstract public void addDeque(ConcurrentLinkedDeque<TaskLet> originDeque);
}

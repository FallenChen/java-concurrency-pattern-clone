package com.garry.features.executors.work_stealing.basic;

import java.util.HashSet;

public interface Scheduler {

    void spawn(TaskLet t);

    // add tasklet to t.master and to add queue
    void waitForAll(HashSet<TaskLet> master);

    // wait for all
    void printStats();
}

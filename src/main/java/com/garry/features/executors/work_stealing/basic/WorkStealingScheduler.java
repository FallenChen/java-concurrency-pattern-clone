package com.garry.features.executors.work_stealing.basic;

import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedDeque;

public class WorkStealingScheduler implements Scheduler{


    @Override
    public void spawn(TaskLet t) {

    }

    @Override
    public void waitForAll(HashSet<TaskLet> master) {

    }

    @Override
    public void printStats() {

    }

    private class ServerThread implements Runnable {

        public ConcurrentLinkedDeque<TaskLet> deque = new ConcurrentLinkedDeque<>();


        @Override
        public void run() {

        }
    }
}

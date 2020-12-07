package com.garry.features.executors.work_stealing.basic;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashSet;
import java.util.Stack;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * https://github.com/annabonaldo/WorkStealingScheduler/blob/master/src/Scheduler.java
 */
public class WorkStealingScheduler implements Scheduler{

    private static AtomicBoolean shutdownNow = new AtomicBoolean(false);

    private ServerThread[] servers;




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
        private ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        // stores the Tasklet the server is running
        private Stack<TaskLet> stack = new Stack<>();
        // the identifier of the server
        private int myIndex = 0;

        // stores the Tasklets the server need to run.When dealing with its own deque,
        // a server always adds and polls from the bottom
        public ConcurrentLinkedDeque<TaskLet> deque = new ConcurrentLinkedDeque<>();
        // about metric
        public final ServerStatistics stats = new ServerStatistics();

        public ServerThread(int myIndex){
            this.myIndex = myIndex;
        }


        // defines the standard behaviour of the server.If the server's deque is void,
        // the server calls the steal method;otherwise it polls the last Tasklet from the bottom of the deque,
        // pushed it into the stack and then execute it
        @Override
        public void run() {
            shutdownNow.set(false);

            long startCpuTime = threadMXBean.getCurrentThreadCpuTime();
            long startClockTimer = System.nanoTime();
            while (!shutdownNow.get())
            {
                if (deque.isEmpty()) {
                    steal();
                }
                while (!deque.isEmpty()){
                    TaskLet t = deque.pollLast();
                    if (t != null){
                        stack.push(t);
                        t.invoke();
                        stack.pop();
                    }
                }
                shutdownNow.set(checkServers());
            }
            this.stats.CPUTime = threadMXBean.getCurrentThreadCpuTime() - startCpuTime;
            this.stats.ClockTime = System.nanoTime() - startClockTimer;
        }

        private boolean checkServers(){

            for (int i = 0; i < servers.length; i++) {
                if (!servers[i].deque.isEmpty() || !servers[i].stack.isEmpty())
                    return false;
            }
            return true;
        }

        // having its own deque empty, the server tries stealing Tasklets from
        // the top of someone else's deque.If the steal is successful the server may proceed with
        // the Tasklet execution
        public boolean steal(){
            boolean stolen = false;

            for (int i = 0; i < servers.length && (!stolen); i++) {
                if (i != myIndex && !servers[i].deque.isEmpty()){
                    TaskLet t = servers[i].deque.pollFirst();
                    if (t != null){
                        servers[myIndex].deque.addLast(t);
                        servers[myIndex].stats.numTaskletSteals++;
                        stolen = true;
                    }
                }
            }
            return stolen;
        }

    }
}

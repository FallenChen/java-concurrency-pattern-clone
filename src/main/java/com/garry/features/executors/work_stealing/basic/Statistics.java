package com.garry.features.executors.work_stealing.basic;

public class Statistics {

    public String[] input = new String[3];
    public SchedulerStatistics concStats = new SchedulerStatistics();
    public SequentialStatistics seqStats = new SequentialStatistics();

}

class ServerStatistics {
    public int numTaskletInitiations = 0;
    public int numTaskletSteals = 0;
    public long CPUTime = 0;
    public long ClockTime = 0;
}

class SchedulerStatistics {
    public int numServers = 0;
    public long totalCPUTime = 0;
    public long totalClockTime = 0;
    public int totalSteals = 0;
}

class SequentialStatistics {
    public long wallClockTime = 0;
    public long CPUTime = 0;
}

package org.nacho.ibr.routing.model;

import java.util.Date;

public class RouteParameters {
    private long minSeconds;
    private long maxSeconds;
    private long stopSeconds;

    private Date startDate;
    private int sleepHours;

    public RouteParameters(long minSeconds, long maxSeconds, long stopSeconds, Date startDate, int sleepHours) {
        this.minSeconds = minSeconds;
        this.maxSeconds = maxSeconds;
        this.stopSeconds = stopSeconds;
        this.startDate = startDate;
        this.sleepHours = sleepHours;
    }

    public RouteParameters() {}

    public long getMinSeconds() {
        return minSeconds;
    }

    public void setMinSeconds(long minSeconds) {
        this.minSeconds = minSeconds;
    }

    public long getMaxSeconds() {
        return maxSeconds;
    }

    public void setMaxSeconds(long maxSeconds) {
        this.maxSeconds = maxSeconds;
    }

    public long getStopSeconds() {
        return stopSeconds;
    }

    public void setStopSeconds(long stopSeconds) {
        this.stopSeconds = stopSeconds;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getSleepHours() {
        return sleepHours;
    }

    public void setSleepHours(int sleepHours) {
        this.sleepHours = sleepHours;
    }
}

package org.nacho.ibr.routing.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RouteParameters {

    public static final String SLEEP = "SLEEP";
    private long minSeconds;
    private long maxSeconds;
    private long stopSeconds;
    private Date startDate;
    private Date endDate;
    private int sleepHours;
    private int startSleeping;

    private long[] beginSleep;

    private long[] endSleep;

    public RouteParameters(long minSeconds, long maxSeconds, long stopSeconds, Date startDate, Date endDate, int sleepHours, int startSleeping) {
        this.minSeconds = minSeconds;
        this.maxSeconds = maxSeconds;
        this.stopSeconds = stopSeconds;
        this.startDate = startDate;
        this.sleepHours = sleepHours;
        this.startSleeping = startSleeping;
        this.endDate = endDate;
    }

    public RouteParameters() {
    }

    public long[] getBeginSleep() {
        if (beginSleep == null) {
            calculateSleep();
        }
        return beginSleep;
    }

    public long[] getEndSleep() {
        if (endSleep == null) {
            calculateSleep();
        }
        return endSleep;
    }

    private void calculateSleep() {
        Calendar calendar = GregorianCalendar.getInstance();

        int q = (int) (maxSeconds / (3600 * 24));

        beginSleep = new long[q];
        endSleep = new long[q];

        calendar.setTime(startDate);
        calendar.set(Calendar.HOUR_OF_DAY, startSleeping);
        calendar.set(Calendar.MINUTE, 0);

        long st = (calendar.getTimeInMillis() - startDate.getTime())/ 1000;

        for (int i=0;  i < q; i++) {
            beginSleep[i] = st;
            endSleep[i] = st + (sleepHours * 3600);
            st += 24 * 3600;
        }
    }

    public int getDays() {
        if (beginSleep == null) {
            calculateSleep();
        }
        return beginSleep.length;
    }

    public long getMinSeconds() {
        return minSeconds;
    }

    public void setMinSeconds(long minSeconds) {
        this.minSeconds = minSeconds;
    }

    public int getStartSleeping() {
        return startSleeping;
    }

    public void setStartSleeping(int startSleeping) {
        this.startSleeping = startSleeping;
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

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getSleepSeconds() {
        return sleepHours * 3600;
    }
}

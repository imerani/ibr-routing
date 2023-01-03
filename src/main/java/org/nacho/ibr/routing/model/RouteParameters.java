package org.nacho.ibr.routing.model;

import org.nacho.ibr.routing.processor.ValueCalculator;

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

    private float increment;

    private long[] beginSleep;

    private long[] endSleep;

    private ValueCalculator valueCalculator;

    public RouteParameters(ValueCalculator calculator) {
        this.valueCalculator = calculator;
    }

    public void calculateLimits(int delta) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(startDate);
        long start = cal.getTimeInMillis() / 1000;
        cal.setTime(endDate);
        long end = cal.getTimeInMillis() / 1000;

        maxSeconds = end - start;
        minSeconds = maxSeconds - (delta * 3600);
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

        long st = (calendar.getTimeInMillis() - startDate.getTime()) / 1000;

        Calendar c = GregorianCalendar.getInstance();


        for (int i = 0; i < q; i++) {
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

    public ValueCalculator getValueCalculator() {
        return valueCalculator;
    }

    public float getIncrement() {
        return increment;
    }

    public void setIncrement(float increment) {
        this.increment = increment;
    }
}

package org.nacho.ibr.routing.model;


public class Distance {
    private long time;
    private long meters;

    public long getTime() {
        return time;
    }

    public Distance() {
    }

    public Distance(long time, long meters) {
        this.time = time;
        this.meters = meters;
    }

    public long getMeters() {
        return meters;
    }

    public void setMeters(long meters) {
        this.meters = meters;
    }

    public void setTime(long time) {
        this.time = time;
    }

}

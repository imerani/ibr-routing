package org.nacho.ibr.routing.model;

import java.util.HashMap;
import java.util.Map;

public class Location {

    private float latitude;
    private float longitude;
    private String name;
    private int points;
    private String icon;
    private String time;
    private String description;

    private int minHour;
    private int maxHour;

    private Location previousLocation;

    public int getMinHour() {
        return minHour;
    }

    public void setMinHour(int minHour) {
        this.minHour = minHour;
    }

    public int getMaxHour() {
        return maxHour;
    }

    public void setMaxHour(int maxHour) {
        this.maxHour = maxHour;
    }

    public Location() {}

    private Map<String, Distance> distances = new HashMap<>();

    public Location(String name, float latitude, float longitude, String icon, int points,  String time, int minHour, int maxHour) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.points = points;
        this.icon = icon;
        this.time = time;
        this.minHour = minHour;
        this.maxHour = maxHour;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Location getPreviousLocation() {
        return previousLocation;
    }

    public void setPreviousLocation(Location previousLocation) {
        this.previousLocation = previousLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Distance> getDistances() {
        return distances;
    }
}

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

    private Map<String, Double> distances = new HashMap<>();

    public Location(String name, float latitude, float longitude, String icon, int points,  String time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.points = points;
        this.icon = icon;
        this.time = time;
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

    public Map<String, Double> getDistances() {
        return distances;
    }
}

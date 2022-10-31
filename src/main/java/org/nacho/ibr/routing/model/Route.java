package org.nacho.ibr.routing.model;

import org.nacho.ibr.routing.processor.RouteValidator;

import java.util.*;

public class Route {

    public RouteValidator getValidator() {
        return validator;
    }

    public RouteParameters getParameters() {
        return parameters;
    }

    private String name;
    private Location start;
    private Location end;

    private RouteValidator validator;

    private RouteParameters parameters;

    private long distance;

    public Route(RouteValidator validator, RouteParameters parameters) {
        this.validator = validator;
        this.parameters = parameters;
    }

    public long getTime() {
        return time;
    }

    private long time;

    public int getStops() {
        return stops;
    }

    public void setStops(int stops) {
        this.stops = stops;
    }

    private int stops;
    private int value;

    public Queue<Location> getPoints() {
        return points;
    }

    private Queue<Location> points = new LinkedList<>();

    public boolean addLocation(Location location) {
        Distance d = validator.validateAddLocation(this, location, parameters);
        if (d != null) {
            this.distance = d.getMeters();
            this.time = d.getTime();
            points.add(location);
            return true;
        }
        return false;
//        long d = 0;
//        int v = 0;
//        long t = 0;
//        Location last = start;
//        for (Location l: points) {
//            v += l.getPoints();
//            d += last.getDistances().get(l.getName()).getMeters();
//            t += last.getDistances().get(l.getName()).getTime();
//            t += stops;
//            last = l;
//            if (location.getName().equals(l.getName())) {
//                return false;
//            }
//        }
//        d += last.getDistances().get(location.getName()).getMeters();
//        t += last.getDistances().get(location.getName()).getTime();
//        t += stops;
//        d += location.getDistances().get(end.getName()).getMeters();
//        t += location.getDistances().get(end.getName()).getTime();
//
//        v += location.getPoints();
//        distance = d;
//        value = v;
//        time = t;
//        points.add(location);
//        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Location getStart() {
        return start;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public Location getEnd() {
        return end;
    }

    public void setEnd(Location end) {
        this.end = end;
    }

    public void setPoints(Queue<Location> points) {
        this.points = points;
    }

    public long getDistance() {
        return distance;
    }

    public int getValue() {
        if (value == 0) {
            for (Location l: points) {
                value += l.getPoints();
            }
        }
        return value;
    }

    public String createHash() {
        List<String> names = new ArrayList<>();
        for (Location l : points) {
            names.add(l.getName());
        }
        Collections.sort(names);
        StringBuffer hash = new StringBuffer();
        for (String s : names) {
            hash.append(s);
        }
        return hash.toString();
    }

    @Override
    public String toString() {
        StringBuffer retorno = new StringBuffer("Total value: ");
        retorno.append(getValue());
        retorno.append("\n");
        retorno.append("Distance: ");
        retorno.append(distance);
        retorno.append("\n");
        retorno.append("Points: \n");
        retorno.append(start.getName());
        retorno.append("\n");
        for (Location l : points) {
            retorno.append(l.getName());
            retorno.append("\n");
        }
        retorno.append(end.getName());
        retorno.append("\n");
        return retorno.toString();
    }
}

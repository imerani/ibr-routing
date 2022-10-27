package org.nacho.ibr.routing.util;

import io.jenetics.jpx.GPX;
import org.nacho.ibr.routing.model.Location;
import org.nacho.ibr.routing.model.Route;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Utilities {

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    public static void addDistances(Map<String, Location> locations) {
        for (Location location : locations.values()) {
            for (Location l : locations.values()) {
                if (location.getName().equals(l.getName())) {
                    continue;
                }
                location.getDistances().put(l.getName(), distance(location.getLatitude(), location.getLongitude(),
                        l.getLatitude(), l.getLongitude()));
            }
        }
    }

    public static Route cloneRoute(Route route) {
        Route r = new Route();
        r.setName(route.getName());
        r.setStart(route.getStart());
        r.setEnd(route.getEnd());
        Queue<Location> q = new LinkedList<>();
        for (Location l : route.getPoints()) {
            q.add(l);
        }
        r.setPoints(q);
        return r;
    }

    public static Map<String, Location> filterPoints(Map<String, Location> locations, int min) {
        Map<String, Location> retorno = new HashMap<>();
        for (Location loc: locations.values()) {
            if (loc.getPoints() > min) {
                retorno.put(loc.getName(), loc);
            }
        }
        return retorno;
    }

    public static List<Route> filterBest(List<Route> routes, int q) {
        if (routes.size() < q) {
            return  routes;
        }
        Collections.sort(routes, new Comparator<Route>() {
            @Override
            public int compare(Route o1, Route o2) {
                if (o1.getValue() == o2.getValue()) {
                    return 0;
                }
                if (o1.getValue() < o2.getValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        List<Route> retorno = new ArrayList<>();
        for(int i=0; i < q; i++) {
            retorno.add(routes.get(i));
        }

        return retorno;
    }

    public static void writeFile(String filename, List<Route> routes) {
        try {
            PrintWriter writer = new PrintWriter(filename);

            for (Route route: routes) {
                writer.println(route);
            }

            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Route> removeDuplicates(List<Route> routes) {
        Map<String, Route> map = new HashMap<>();

        for (Route r: routes) {
            String hash = r.createHash();
            if (map.containsKey(hash)) {
                Route r2 = map.get(hash);
                if (r2.getDistance() < r.getDistance()) {
                    map.put(hash, r2);
                }
            } else {
                map.put(hash, r);
            }
        }
        return new ArrayList<Route>(map.values());
    }

}

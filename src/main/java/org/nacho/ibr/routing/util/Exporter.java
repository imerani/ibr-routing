package org.nacho.ibr.routing.util;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;
import org.nacho.ibr.routing.model.Location;
import org.nacho.ibr.routing.model.Route;
import org.nacho.ibr.routing.model.RouteParameters;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.List;

public class Exporter {

    public static void exportRoutes(String filename, List<org.nacho.ibr.routing.model.Route> routes,
                                    Location[] locations, RouteParameters parameters) {

        GPX.Builder builder = GPX.builder();
        for (org.nacho.ibr.routing.model.Route route: routes) {
            io.jenetics.jpx.Route.Builder rb = io.jenetics.jpx.Route.builder();
            rb.name("Points: " + route.getValue() + " Distance: " + route.getDistance() / 1600 + " Time: " + secondsToTime(route.getTime()));
            Location start = route.getStart();
            rb.addPoint(WayPoint.builder().lat(start.getLatitude()).lon(start.getLongitude())
                    .name(start.getName()).cmt(start.getDescription()).build());
            Location last = start;
            long time = 0;
            for (Location location: route.getPoints()) {
                if (location.getName().equals(RouteParameters.SLEEP)) {
                    time += parameters.getSleepSeconds();
                } else {
                    time += last.getDistances().get(location.getName()).getTime();
                    last = location;
                }
                rb.addPoint(WayPoint.builder().lat(location.getLatitude()).lon(location.getLongitude())
                        .name(createPointName(location)).cmt(createPointDescription(location, parameters, time)).build());
            }
            Location end = route.getEnd();
            if (last.getName().equals(RouteParameters.SLEEP)) {
                time += parameters.getSleepSeconds();
            } else {
                time += last.getDistances().get(end.getName()).getTime();
            }
            rb.addPoint(WayPoint.builder().lat(end.getLatitude()).lon(end.getLongitude())
                    .name(end.getName()).cmt(createPointDescription(end, parameters, time)).build());

            builder.addRoute(rb.build());
        }
        for (Location location: locations) {
            builder.addWayPoint(WayPoint.builder().lat(location.getLatitude()).lon(location.getLongitude())
                    .name(createPointName(location)).cmt(location.getDescription()).build());
        }
        GPX gpx = builder.build();
        try {
            GPX.write(gpx, FileSystems.getDefault().getPath(filename + ".gpx"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//        int i = 0;
//        for (Route route: routes) {
//            GPX.Builder builder = GPX.builder();
//            Location start = route.getStart();
//            builder.addWayPoint(WayPoint.builder().lat(start.getLatitude()).lon(start.getLongitude())
//                    .name(start.getName()).cmt(start.getDescription()).build());
//            for (Location location: route.getPoints()) {
//                builder.addWayPoint(WayPoint.builder().lat(location.getLatitude()).lon(location.getLongitude())
//                        .name(location.getName()).cmt(location.getDescription()).build());
//            }
//            Location end = route.getEnd();
//            builder.addWayPoint(WayPoint.builder().lat(end.getLatitude()).lon(end.getLongitude())
//                    .name(end.getName()).cmt(end.getDescription()).build());
//            try {
//                GPX.write(builder.build(), new FileOutputStream(filename + i + ".gpx"));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//            i++;
//        }


    }

    private static String createPointName(Location l) {
        return l.getName() + "-" + l.getPoints() + "-" + l.getTime();
    }

    private static String secondsToTime(long totalSecs) {
        long hours = totalSecs / 3600;
        long minutes = (totalSecs % 3600) / 60;
        long seconds = totalSecs % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private static String createPointDescription(Location location, RouteParameters parameters, long time) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(parameters.getStartDate());
        cal.add(Calendar.SECOND, (int) time);
        String startTime = cal.getTime().toString();
        cal.add(Calendar.SECOND, (int) location.getStopTime());
        String endTime = cal.getTime().toString();
        return "Arrival: " + startTime + " - Departure: " + endTime;
    }
}

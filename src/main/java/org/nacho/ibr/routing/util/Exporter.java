package org.nacho.ibr.routing.util;

import io.jenetics.jpx.GPX;
import io.jenetics.jpx.WayPoint;
import org.nacho.ibr.routing.model.Location;
import org.nacho.ibr.routing.model.Route;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.List;

public class Exporter {

    public static void exportRoutes(String filename, List<org.nacho.ibr.routing.model.Route> routes) {
        GPX.Builder builder = GPX.builder();
        for (org.nacho.ibr.routing.model.Route route: routes) {
            io.jenetics.jpx.Route.Builder rb = io.jenetics.jpx.Route.builder();
            Location start = route.getStart();
            rb.addPoint(WayPoint.builder().lat(start.getLatitude()).lon(start.getLongitude())
                    .name(start.getName()).cmt(start.getDescription()).build());
            for (Location location: route.getPoints()) {
                rb.addPoint(WayPoint.builder().lat(location.getLatitude()).lon(location.getLongitude())
                        .name(location.getName()).cmt(location.getDescription()).build());
            }
            Location end = route.getEnd();
            rb.addPoint(WayPoint.builder().lat(end.getLatitude()).lon(end.getLongitude())
                    .name(end.getName()).cmt(end.getDescription()).build());
            builder.addRoute(rb.build());
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
}

package org.nacho.ibr.routing.processor;

import org.nacho.ibr.routing.model.Distance;
import org.nacho.ibr.routing.model.Location;
import org.nacho.ibr.routing.model.Route;
import org.nacho.ibr.routing.model.RouteParameters;

public class FastRouteValidator implements RouteValidator {

    @Override
    public boolean validateRoute(Route route, RouteParameters parameters) {
        return route.getTime() < parameters.getMaxSeconds();
    }

    @Override
    public Distance addLocation(Route route, Location location, RouteParameters parameters) {
        long distance = 0;
        long time = 0;
        Location last = route.getStart();
        for (Location loc: route.getPoints()) {
            if (location.getName().equals(loc.getName())) return null;
            distance += last.getDistances().get(loc.getName()).getMeters();
            time += last.getDistances().get(loc.getName()).getTime();
            time += parameters.getStopSeconds();
            if (time > parameters.getMaxSeconds()) {
                return null;
            }
            last = loc;
        }
        distance += location.getDistances().get(last.getName()).getMeters();
        time += location.getDistances().get(last.getName()).getTime();
        distance += location.getDistances().get("END").getMeters();
        time += location.getDistances().get("END").getTime();
        if (time > parameters.getMaxSeconds()) {
            return null;
        }
        route.getPoints().add(location);
        return new Distance(time, distance);
    }
}

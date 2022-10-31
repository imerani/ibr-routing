package org.nacho.ibr.routing.processor;

import org.nacho.ibr.routing.model.Distance;
import org.nacho.ibr.routing.model.Location;
import org.nacho.ibr.routing.model.Route;
import org.nacho.ibr.routing.model.RouteParameters;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeRouteValidator implements RouteValidator {

    @Override
    public boolean validateRoute(Route route, RouteParameters parameters) {
        return route.getTime() < parameters.getMaxSeconds();
    }

    @Override
    public Distance validateAddLocation(Route route, Location location, RouteParameters parameters) {
        Calendar calendar = GregorianCalendar.getInstance();
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
        if (!validateTime(calendar, parameters.getStartDate(), time, location)) return null;
        return new Distance(time, distance);
    }

    private boolean validateTime(Calendar calendar, Date start, long seconds, Location location) {
        if (location.getMinHour() == 0 && location.getMaxHour() == 24) return true;
        calendar.setTime(start);
        calendar.add(Calendar.SECOND, (int) seconds);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return (hour >= location.getMinHour() && hour <= location.getMaxHour());
    }
}

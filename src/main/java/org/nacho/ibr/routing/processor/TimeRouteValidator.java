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
    public Distance addLocation(Route route, Location location, RouteParameters parameters) {
        Calendar calendar = GregorianCalendar.getInstance();
        long distance = 0;
        long time = 0;
        Location last = route.getStart();
        for (Location loc: route.getPoints()) {
            if (location.getName().equals(loc.getName())) return null;
            if (loc.getName().equals(RouteParameters.SLEEP)) {
                time += parameters.getSleepSeconds();
                continue;
            }
            distance += last.getDistances().get(loc.getName()).getMeters();
            time += last.getDistances().get(loc.getName()).getTime();
            time += parameters.getStopSeconds();
            if (time > parameters.getMaxSeconds()) {
                return null;
            }
            last = loc;
        }

        boolean needSleep = time > getNextSleep(route, parameters);
        distance += location.getDistances().get(last.getName()).getMeters();
        time += location.getDistances().get(last.getName()).getTime();

        boolean valid = false;
        boolean sleepFirst = false;
        if (validateTime(calendar, parameters.getStartDate(), time, location)) valid = true;

        if (!valid && needSleep) {
            if (validateTime(calendar, parameters.getStartDate(), time + parameters.getSleepSeconds(), location)) {
                valid = true;
                sleepFirst = true;
            }
        }
        if (!valid) return null;

        if (needSleep) {
            time += parameters.getSleepSeconds();
        }
        distance += location.getDistances().get("END").getMeters();
        time += location.getDistances().get("END").getTime();
        if (time > parameters.getMaxSeconds()) {
            return null;
        }
        if (needSleep) {
            setNextSleep(route);
            if (sleepFirst) {
                route.getPoints().add(createSleepLocation(location));
                route.getPoints().add(location);
            } else {
                route.getPoints().add(location);
                route.getPoints().add(createSleepLocation(location));
            }
        } else {
            route.getPoints().add(location);
        }

        return new Distance(time, distance);
    }

    private long getNextSleep(Route route, RouteParameters parameters) {
        for (int i=0; i < route.getSlept().length; i++) {
            if (!route.getSlept()[i]) {
                return parameters.getBeginSleep()[i];
            }
        }
        throw new RuntimeException("No more sleep days");
    }

    private void setNextSleep(Route route) {
        for (int i=0; i < route.getSlept().length; i++) {
            if (!route.getSlept()[i]) {
                route.getSlept()[i] = true;
                break;
            }
        }
    }

    private Location createSleepLocation(Location location) {
        return new Location(RouteParameters.SLEEP, location.getLatitude(), location.getLongitude(), location.getIcon(), 0, "",0,0);
    }

    private boolean validateTime(Calendar calendar, Date start, long seconds, Location location) {
        if (location.getMinHour() == 0 && location.getMaxHour() == 24) return true;
        calendar.setTime(start);
        calendar.add(Calendar.SECOND, (int) seconds);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return (hour >= location.getMinHour() && hour <= location.getMaxHour());
    }
}

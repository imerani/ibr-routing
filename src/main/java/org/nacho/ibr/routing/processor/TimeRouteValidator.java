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
        long distance = route.getPreviousDistance().getMeters();
        long time = route.getPreviousDistance().getTime();
        Location last;
        if (route.getPoints().isEmpty()) {
            last = route.getStart();
        } else {
            last = route.getPoints().peekLast();
        }

        for (Location loc : route.getPoints()) {
            if (location.getName().equals(loc.getName())) return null;
//            if (loc.getName().equals(RouteParameters.SLEEP)) {
//                time += parameters.getSleepSeconds();
//                continue;
//            }
//            distance += last.getDistances().get(loc.getName()).getMeters();
//            time += last.getDistances().get(loc.getName()).getTime();
//            time += parameters.getStopSeconds();
//            if (time > parameters.getMaxSeconds()) {
//                return null;
//            }
//            last = loc;
        }
        long nextSleep = getNextSleep(route, parameters);
        boolean needSleep = time > nextSleep;
        if (last.getName().equals(RouteParameters.SLEEP)) {
            last = last.getPreviousLocation();
        }

//        if (route.getPoints().size() == 4 && route.getPoints().getFirst().getName().equals("OW2") &&
//                route.getPoints().getLast().getName().equals("BHC")) {
//            Location[] loc = new Location[route.getPoints().size()];
//            route.getPoints().toArray(loc);
//
//            if (loc[1].getName().equals("HFM") && loc[2].getName().equals("PEE") && location.getName().equals("TCH")) {
//
//                System.out.println("here");
//            }
//        }

        distance += location.getDistances().get(last.getName()).getMeters();
        time += location.getDistances().get(last.getName()).getTime();

        long previousDistance = distance;
        long previousTime = time;

        distance += location.getDistances().get("END").getMeters();
        time += location.getDistances().get("END").getTime();

        boolean valid;
        boolean sleepFirst = false;
        valid = (validateTime(calendar, parameters.getStartDate(), previousTime, location));

        if (!valid && needSleep) {
            if (validateTime(calendar, parameters.getStartDate(), previousTime + parameters.getSleepSeconds(), location)) {
                valid = true;
                sleepFirst = true;
            }
        }

        if (valid && !needSleep && previousTime > nextSleep) {
            needSleep = true;

        }
        if (!valid) return null;

        if (needSleep) {
            time += parameters.getSleepSeconds();
        }

        if (time > parameters.getMaxSeconds()) {
            return null;
        }
        if (needSleep) {
            previousTime += parameters.getSleepSeconds();
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

        route.setPreviousDistance(new Distance(previousTime, previousDistance));

        return new Distance(time, distance);
    }

    private long getNextSleep(Route route, RouteParameters parameters) {
        for (int i = 0; i < route.getSlept().length; i++) {
            if (!route.getSlept()[i]) {
                return parameters.getBeginSleep()[i];
            }
        }
        return 240 * 3600;
    }

    private void setNextSleep(Route route) {
        for (int i = 0; i < route.getSlept().length; i++) {
            if (!route.getSlept()[i]) {
                route.getSlept()[i] = true;
                break;
            }
        }
    }

    private Location createSleepLocation(Location location) {
        Location loc = new Location(RouteParameters.SLEEP, location.getLatitude(), location.getLongitude(), location.getIcon(), 0, "", 0, 0);
        loc.setPreviousLocation(location);
        return loc;
    }

    private boolean validateTime(Calendar calendar, Date start, long seconds, Location location) {
        if (location.getMinHour() == 0 && location.getMaxHour() == 24) return true;
        calendar.setTime(start);
        calendar.add(Calendar.SECOND, (int) seconds);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        return (hour >= location.getMinHour() && hour <= location.getMaxHour());
    }
}

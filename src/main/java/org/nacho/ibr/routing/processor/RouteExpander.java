package org.nacho.ibr.routing.processor;

import org.nacho.ibr.routing.model.Location;
import org.nacho.ibr.routing.model.Route;
import org.nacho.ibr.routing.util.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteExpander extends Thread {
    private Route route;
    private Map<String, Location> locations;
    private double max;
    private double min;

    public Route getWinner() {
        return winner;
    }

    private Route winner;

    public List<Route> getResult() {
        return result;
    }

    private List<Route> result = new ArrayList<>();

    public List<Route> getWinners() {
        return winners;
    }

    private List<Route> winners = new ArrayList<>();

    public RouteExpander(Route route, Map<String, Location> locations, double min, double max) {
        this.route = route;
        this.locations = locations;
        this.max = max;
        this.min = min;
    }

    @Override
    public void run() {
        for (Location l : locations.values()) {
            Route r = Utilities.cloneRoute(route);
            if (r.addLocation(l) && r.getDistance() < max) {
                result.add(r);
            }
        }
        int best = 0;
        for (Route r: result) {
            if (r.getDistance() > min && r.getDistance() < max) {
                if (r.getValue() > best) {
                    best = r.getValue();
                    winner = r;
                }
                winners.add(r);
            }
        }

    }
}

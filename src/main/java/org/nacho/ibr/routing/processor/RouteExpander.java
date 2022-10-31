package org.nacho.ibr.routing.processor;

import org.nacho.ibr.routing.model.Location;
import org.nacho.ibr.routing.model.Route;
import org.nacho.ibr.routing.model.RouteParameters;
import org.nacho.ibr.routing.util.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteExpander extends Thread {
    private Route route;
    private Map<String, Location> locations;

    public Route getWinner() {
        return winner;
    }

    private Route winner;

    public List<Route> getResult() {
        return result;
    }

    private List<Route> result = new ArrayList<>();


    private RouteParameters parameters;

    public RouteExpander(Route route, Map<String, Location> locations, RouteParameters parameters) {
        this.route = route;
        this.locations = locations;
        this.parameters = parameters;
    }

    @Override
    public void run() {
        for (Location l : locations.values()) {
            Route r = Utilities.cloneRoute(route);
            if (r.addLocation(l)) {
                result.add(r);
            }
        }
        int best = 0;
        for (Route r: result) {
            if (r.getTime() > parameters.getMinSeconds() && r.getTime() < parameters.getMaxSeconds()) {
                if (r.getValue() > best) {
                    best = r.getValue();
                    winner = r;
                }
            }
        }

    }
}

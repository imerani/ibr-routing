package org.nacho.ibr.routing.processor;

import org.nacho.ibr.routing.model.Distance;
import org.nacho.ibr.routing.model.Location;
import org.nacho.ibr.routing.model.Route;
import org.nacho.ibr.routing.model.RouteParameters;

public interface RouteValidator {

    public boolean validateRoute(Route route, RouteParameters parameters);

    public Distance validateAddLocation(Route route, Location location, RouteParameters parameters);
}

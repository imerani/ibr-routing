package org.nacho.ibr.routing.processor;

import org.nacho.ibr.routing.model.Location;
import org.nacho.ibr.routing.model.Route;

public class DefaultValueCalculator implements ValueCalculator {
    @Override
    public int calculateValue(Route route) {
        int value = 0;
        for (Location l : route.getPoints()) {
            value += l.getPoints();
        }
        return value;
    }
}

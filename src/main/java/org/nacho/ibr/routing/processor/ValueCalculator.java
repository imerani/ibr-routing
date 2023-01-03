package org.nacho.ibr.routing.processor;

import org.nacho.ibr.routing.model.Route;

public interface ValueCalculator {

    public int calculateValue(Route route);
}

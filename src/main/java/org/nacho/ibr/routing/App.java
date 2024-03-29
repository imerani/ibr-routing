package org.nacho.ibr.routing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.internal.ratelimiter.Stopwatch;
import org.nacho.ibr.routing.model.Location;
import org.nacho.ibr.routing.model.Route;
import org.nacho.ibr.routing.model.RouteParameters;
import org.nacho.ibr.routing.processor.*;
import org.nacho.ibr.routing.util.Exporter;
import org.nacho.ibr.routing.util.Utilities;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws InterruptedException, IOException {
        // Parameters
        int stops = 20 * 60;
        int startSleeping = 23;

        int numPoints = 25;

        Calendar cal = Calendar.getInstance();
        cal.set(2022, Calendar.JULY, 1, 10, 0);
        Date startDate = cal.getTime();
        cal.set(2022, Calendar.JULY, 4, 22, 0);
        Date endDate = cal.getTime();

        ValueCalculator valueCalculator = new DefaultValueCalculator();

        Stopwatch timer = Stopwatch.createStarted();
        //CSVReader reader = new CSVReader("leg1.csv");
        ObjectMapper objectMapper = new ObjectMapper();
        Location[] l = objectMapper.readValue(new File("testtimes.json"), Location[].class);

        Map<String, Location> locations = new HashMap<>();
        //Utilities.addDistances(locations);
        for (int i = 0; i < l.length; i++) {
            locations.put(l[i].getName(), l[i]);
        }
        //RouteValidator validator = new FastRouteValidator();
        RouteValidator validator = new TimeRouteValidator();
        RouteParameters parameters = new RouteParameters(valueCalculator);
        parameters.setSleepHours(4);
        parameters.setStopSeconds(stops);
        parameters.setStartSleeping(startSleeping);

        parameters.setStartDate(startDate);
        parameters.setEndDate(endDate);
        parameters.calculateLimits(4);

        parameters.setIncrement(0.2f);

        Utilities.updateLocations(l, parameters);

        Route initial = new Route(validator, parameters);
        List<Route> routes = new ArrayList<>();
        List<Route> winners = new ArrayList<>();
        winners.add(initial);
        routes.add(initial);
        Location start = locations.get("START");
        Location end = locations.get("END");
        locations.remove("START");
        locations.remove("END");

        locations = Utilities.filterPoints(locations, numPoints, true);

        initial.setStart(start);
        initial.setEnd(end);

        int i = 0;
        while (!routes.isEmpty()) {
            List<RouteExpander> expanders = new ArrayList<>();
            for (Route route : routes) {
                RouteExpander expander = new RouteExpander(route, locations, parameters);
                expanders.add(expander);
                expander.start();
            }

            routes.clear();
            for (RouteExpander expander : expanders) {
                expander.join();
                routes.addAll(expander.getResult());
                if (expander.getWinner() != null) {
                    winners.add(expander.getWinner());
                }
            }

            routes = Utilities.removeDuplicates(routes);
            winners = Utilities.removeDuplicates(winners);

            winners = Utilities.filterBest(winners, 10);

            Utilities.writeFile("result" + i, winners);
            System.out.println("Routes: " + routes.size());
            System.out.println("Winners: " + winners.size());
            i++;
        }

        Exporter.exportRoutes("routes", winners, l, parameters);
        System.out.println("Time: " + timer.stop());
    }
}

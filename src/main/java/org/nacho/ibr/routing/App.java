package org.nacho.ibr.routing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.internal.ratelimiter.Stopwatch;
import jdk.jshell.spi.ExecutionControl;
import org.nacho.ibr.routing.model.Location;
import org.nacho.ibr.routing.model.Route;
import org.nacho.ibr.routing.model.RouteParameters;
import org.nacho.ibr.routing.processor.FastRouteValidator;
import org.nacho.ibr.routing.processor.RouteExpander;
import org.nacho.ibr.routing.processor.RouteValidator;
import org.nacho.ibr.routing.processor.TimeRouteValidator;
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
        int minHours = 60;
        int maxHours = 64;
        int stops = 20 * 60;

        int minpoints = 399;

        Stopwatch timer = Stopwatch.createStarted();
        //CSVReader reader = new CSVReader("leg1.csv");
        ObjectMapper objectMapper = new ObjectMapper();
        Location[] l = objectMapper.readValue(new File("testtimes.json"), Location[].class);
        Map<String, Location> locations = new HashMap<>();
        //Utilities.addDistances(locations);
        for (int i=0; i < l.length; i++) {
            locations.put(l[i].getName(), l[i]);
        }
        //RouteValidator validator = new FastRouteValidator();
        RouteValidator validator = new TimeRouteValidator();
        RouteParameters parameters = new RouteParameters();
        parameters.setMaxSeconds(maxHours * 3600);
        parameters.setMinSeconds(minHours * 3600);
        parameters.setSleepHours(4);
        parameters.setStopSeconds(stops);

        Calendar cal = Calendar.getInstance();
        cal.set(2022, 6, 1, 10, 00);
        parameters.setStartDate(cal.getTime());

        Route initial = new Route(validator, parameters);
        List<Route> routes = new ArrayList<>();
        List<Route> winners = new ArrayList<>();
        winners.add(initial);
        routes.add(initial);
        Location start = locations.get("START");
        Location end = locations.get("END");
        locations.remove("START");
        locations.remove("END");

        locations = Utilities.filterPoints(locations, minpoints);

        initial.setStart(start);
        initial.setEnd(end);

        int i = 0;
        while (!routes.isEmpty()) {
            List<RouteExpander> expanders = new ArrayList<>();
            for (Route route: routes) {
                RouteExpander expander = new RouteExpander(route, locations, parameters);
                expanders.add(expander);
                expander.start();
            }

            routes.clear();
            for (RouteExpander expander: expanders) {
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

        Exporter.exportRoutes("routes", winners, l);
        System.out.println("Time: " + timer.stop());
    }
}

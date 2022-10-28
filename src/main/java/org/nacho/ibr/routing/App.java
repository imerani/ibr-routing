package org.nacho.ibr.routing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.internal.ratelimiter.Stopwatch;
import jdk.jshell.spi.ExecutionControl;
import org.nacho.ibr.routing.model.Location;
import org.nacho.ibr.routing.model.Route;
import org.nacho.ibr.routing.processor.RouteExpander;
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
        int minHours = 68;
        int maxHours = 72;
        int stops = 20 * 60;

        int minpoints = 499;

        Stopwatch timer = Stopwatch.createStarted();
        //CSVReader reader = new CSVReader("leg1.csv");
        ObjectMapper objectMapper = new ObjectMapper();
        Location[] l = objectMapper.readValue(new File("leg1.json"), Location[].class);
        Map<String, Location> locations = new HashMap<>();
        //Utilities.addDistances(locations);
        for (int i=0; i < l.length; i++) {
            locations.put(l[i].getName(), l[i]);
        }

        Route initial = new Route();
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
        int min = minHours * 3600;
        int max = maxHours * 3600;
        int i = 0;
        while (!routes.isEmpty()) {
            List<RouteExpander> expanders = new ArrayList<>();
            for (Route route: routes) {
                RouteExpander expander = new RouteExpander(route, locations, min, max, stops);
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
            System.out.println("Removing duplicates");
            routes = Utilities.removeDuplicates(routes);
            winners = Utilities.removeDuplicates(winners);

            winners = Utilities.filterBest(winners, 10);

            Utilities.writeFile("result" + i, winners);
            System.out.println("Routes: " + routes.size());
            System.out.println("Winners: " + winners.size());
            i++;
        }

        Exporter.exportRoutes("routes", winners);
        System.out.println("Time: " + timer.stop());
    }
}

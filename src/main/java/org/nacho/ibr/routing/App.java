package org.nacho.ibr.routing;

import org.nacho.ibr.routing.model.Location;
import org.nacho.ibr.routing.model.Route;
import org.nacho.ibr.routing.parser.CSVReader;
import org.nacho.ibr.routing.processor.RouteExpander;
import org.nacho.ibr.routing.util.Exporter;
import org.nacho.ibr.routing.util.Utilities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        double min = 3000;
        double max = 3500;
        int minpoints = 499;

        CSVReader reader = new CSVReader("leg1.csv");
        Map<String, Location> locations = reader.getLocations();
        Utilities.addDistances(locations);

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
        int i = 0;
        while (!routes.isEmpty()) {
            List<RouteExpander> expanders = new ArrayList<>();
            for (Route route: routes) {
                RouteExpander expander = new RouteExpander(route, locations, min, max);
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
    }
}

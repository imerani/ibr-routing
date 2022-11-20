package org.nacho.ibr.routing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.Unit;
import org.nacho.ibr.routing.model.Distance;
import org.nacho.ibr.routing.model.Location;
import org.nacho.ibr.routing.parser.CSVReader;

import java.io.File;
import java.io.IOException;

public class Maps {

    public static void main(String[] args) throws IOException, InterruptedException, ApiException {
        CSVReader reader = new CSVReader("leg1.csv");

        reader.getLocations();

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("-UBmvRrvWchCA")
                .build();

        for (Location l1: reader.getLocations().values()) {
            for (Location l2 : reader.getLocations().values()) {
                if (l2.getName().equals(l1.getName())) continue;
                String[] origins = new String[]{Float.toString(l1.getLatitude()) + "," + l1.getLongitude()};
                String[] destinations = new String[]{Float.toString(l2.getLatitude()) + "," + l2.getLongitude()};
                DistanceMatrix result = DistanceMatrixApi.getDistanceMatrix(context, origins, destinations).units(Unit.IMPERIAL).await();
                Distance d = new Distance();
                long meters = result.rows[0].elements[0].distance.inMeters;
                long seconds = result.rows[0].elements[0].duration.inSeconds;
                d.setMeters(meters);
                d.setTime(seconds);
                l1.getDistances().put(l2.getName(), d);
            }
        }
// Invoke .shutdown() after your application is done making requests
        context.shutdown();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(new File("test.json"), reader.getLocations().values());



    }
}

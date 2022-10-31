package org.nacho.ibr.routing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nacho.ibr.routing.model.Location;
import org.nacho.ibr.routing.parser.CSVReader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Loader {

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Location[] l = objectMapper.readValue(new File("leg1.json"), Location[].class);

        CSVReader reader = new CSVReader("leg1time.csv");

        for (Location location: l) {
            Location l2 = reader.getLocations().get(location.getName());
            location.setMinHour(l2.getMinHour());
            location.setMaxHour(l2.getMaxHour());
        }


        objectMapper.writeValue(new File("testtimes.json"), l);
        System.out.println(l);
    }
}

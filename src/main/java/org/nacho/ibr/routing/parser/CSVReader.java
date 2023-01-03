package org.nacho.ibr.routing.parser;

import org.nacho.ibr.routing.model.Location;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSVReader {
    private String file;

    public Map<String, Location> getLocations() {
        return locations;
    }

    private Map<String, Location> locations;

    public CSVReader(String file) {
        this.file = file;
        locations = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Location l = new Location(values[0].trim(), Float.parseFloat(values[1]),
                        Float.parseFloat(values[2]), values[3],
                        Integer.parseInt(values[4]), values[5], Integer.parseInt(values[6]), Integer.parseInt(values[7]));
                locations.put(l.getName(), l);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


}

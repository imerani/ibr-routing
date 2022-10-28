package org.nacho.ibr.routing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nacho.ibr.routing.model.Location;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Loader {

    public static void main(String[] args) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Location[] l = objectMapper.readValue(new File("test.json"), Location[].class);

        System.out.println(l);
    }
}

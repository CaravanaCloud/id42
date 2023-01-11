package id42.service;

import id42.entity.Location;

public class LocationService {
    public static Location of(String text) {
        var loc = parseLoc(text);
        return loc;
    }

    private static Location parseLoc(String text) {
        var loc = Location.of(text);

        return loc;
    }
}

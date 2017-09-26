package com.pikup.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Armando on 9/23/17.
 * A list of locations for a certain type of sport
 * TODO: Add GPS Locations
 */

public class SportsLocations {

    private String game;
    private List<String> locations;

    public SportsLocations() { locations = new ArrayList<>(); }
    public SportsLocations(String game, List<String> locations) {
        this.game = game;
        this.locations = locations;
        if (this.locations == null) {
            this.locations = new ArrayList<>();
        }
    }

    public String getGame() { return game; }

    public List<String> getLocations() { return locations; }

    public void setLocations(List<String> locations) { this.locations = locations; }

    public String toString() { return game; }

    public boolean equals(Object o) {
        if (o instanceof String) {
            return (o.equals(game));
        }
        SportsLocations s = (SportsLocations) o;
        return (s.getGame().equals(game) && s.getLocations().equals(locations));
    }

}

package com.pikup.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Altan on 9/24/2017.
 */

public class Game {

    private String sport;
    private Date timeOfGame;
    private String locationTitle;
    private double locationLatitude;
    private double locationLongitude;
    private int capacity;
    private int intensity;
    private String gameStatus;
    private String hostUID;
    private List<String> playerUIDList;

    public Game() {
        this.sport = "";
        this.timeOfGame = new Date();
        this.locationTitle = "";
        this.locationLatitude = 0;
        this.locationLongitude = 0;
        this.capacity = 0;
        this.intensity = 0;
        this.gameStatus = "";
        this.hostUID = "";
        this.playerUIDList = new ArrayList<>();
    }

    public Game(String sport, Date timeOfGame, String locationTitle, double locationLatitude, double locationLongitude, int capacity ,int intensity, String gameStatus, String hostUID, ArrayList<String> playerUIDList) {
        this.sport = sport;
        this.timeOfGame = timeOfGame;
        this.locationTitle = locationTitle;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
        this.capacity = capacity;
        this.intensity = intensity;
        this.gameStatus = gameStatus;
        this.hostUID = hostUID;
        this.playerUIDList = playerUIDList;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public Date getTimeOfGame() {
        return timeOfGame;
    }

    public void setTimeOfGame(Date timeOfGame) {
        this.timeOfGame = timeOfGame;
    }

    public String getLocationTitle() {
        return locationTitle;
    }

    public void setLocationTitle(String locationTitle) {
        this.locationTitle = locationTitle;
    }

    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public double getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public int getCapacity() { return capacity; }

    public void setCapacity(int capacity) { this.capacity = capacity; }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }

    public String getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }

    public String getHostUID() {
        return hostUID;
    }

    public void setHostUID(String hostUID) {
        this.hostUID = hostUID;
    }

    public List<String> getPlayerUIDList() {
        return playerUIDList;
    }

    public void setPlayerUIDList(ArrayList<String> playerUIDList) {
        this.playerUIDList = playerUIDList;
    }
}

package com.pikup.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Altan on 9/24/2017.
 */

public class Game {

    private String sport;
    private Date timeOfGame;
    private String locationTitle;
    private double locationLatitude;
    private double locationLongitude;
    private String intensity;
    private String gameStatus;
    private String hostUID;
    private ArrayList<String> playerUIDList;

    public Game(String sport, Date timeOfGame, String locationTitle, double locationLatitude, double locationLongitude, String intensity, String gameStatus, String hostUID, ArrayList<String> playerUIDList) {
        this.sport = sport;
        this.timeOfGame = timeOfGame;
        this.locationTitle = locationTitle;
        this.locationLatitude = locationLatitude;
        this.locationLongitude = locationLongitude;
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

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
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

    public ArrayList<String> getPlayerUIDList() {
        return playerUIDList;
    }

    public void setPlayerUIDList(ArrayList<String> playerUIDList) {
        this.playerUIDList = playerUIDList;
    }
}

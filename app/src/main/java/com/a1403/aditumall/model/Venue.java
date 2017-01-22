package com.a1403.aditumall.model;

/**
 * Created by dakfu on 1/21/2017.
 */

public class Venue {


    private String venueId;
    private String name;
    private AccessibilityPoint aps[];
    private Reliability epiPen;
    private Reliability aed;
    private Reliability bathrooms;
    private double lat;
    private double lon;

    public static final String HAS_EPI_PENS = "has epi pens";
    public static final String HAS_AED = "has aed";
    public static final String HAS_BATHROOMS = "has bathrooms";

    public Venue(String id, String name, AccessibilityPoint aps[], Reliability epi,
                 Reliability aed,Reliability bathrooms, double lat, double lon){
        this.venueId = id;
        this.name = name;
        this.aps = aps;
        this.epiPen = epi;
        this.aed = aed;
        this.bathrooms = bathrooms;
    }

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccessibilityPoint[] getAps() {
        return aps;
    }

    public void setAps(AccessibilityPoint[] aps) {
        this.aps = aps;
    }

    public Reliability getEpiPen() {
        return epiPen;
    }

    public void setEpiPen(Reliability epiPen) {
        this.epiPen = epiPen;
    }

    public Reliability getAed() {
        return aed;
    }

    public void setAed(Reliability aed) {
        this.aed = aed;
    }

    public Reliability getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Reliability bathrooms) {
        this.bathrooms = bathrooms;
    }
}

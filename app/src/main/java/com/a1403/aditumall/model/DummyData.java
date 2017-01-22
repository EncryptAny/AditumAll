package com.a1403.aditumall.model;

/**
 * Created by Cory on 1/22/2017.
 */

public class DummyData {

    public static AccessibilityPoint[] getDummyAps()
    {
        AccessibilityPoint[] aps;
        aps = new AccessibilityPoint[3];
        aps[0] = new AccessibilityPoint("Accessible Door",6,2,29.651069,-82.342808);
        aps[1] = new AccessibilityPoint("Hndcpd Parking", 4,1,29.651619,-82.342319);
        aps[2] = new AccessibilityPoint("Wheelchair Ramp",11,3,29.651365,-82.342519);
        return aps;
    }

    public static Venue getDummyVenue_Libwest() {
        return new Venue("lw","Library West", getDummyAps(),null,null,new Reliability(14,1),29.651489,-82.342921);
    }
    public static Venue getDummyVenue_Marston() {
        return new Venue("mstn","Marston", getDummyAps(),null,null,new Reliability(14,1),29.657,-82.3437903);
    }
}

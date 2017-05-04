/*
 * TCSS 450 A Spring 2017
 * Project: Beer Butler App
 * Group 10: Ben, Chris, Tom, and Garrett
 */
package group10.tcss450.uw.edu.challengeapp.BrewTour;

import android.media.Image;

import org.json.JSONObject;

public class BreweryData {
    public static final String DATA = "Stuff";
    private String name;
    private String distance;
    private String hours;
    private Image icon;
    public BreweryData set(JSONObject j) {
        return null;
    }

    public String getName() {
        return name;
    }

    public String getDistance() {
        return distance;
    }

    public String getHours() {
        return hours;
    }

    public Image getIcon() {
        return icon;
    }
}

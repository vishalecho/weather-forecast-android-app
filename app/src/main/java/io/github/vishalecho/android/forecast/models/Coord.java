package io.github.vishalecho.android.forecast.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author vishal kumar
 * @version 1.0
 * @since 06.01.2019
 */

public class Coord {

    @SerializedName("lat")
    @Expose
    private float lat;
    @SerializedName("lon")
    @Expose
    private float lon;

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

}

package io.github.vishalecho.android.forecast.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author vishal kumar
 * @version 1.0
 * @since 06.01.2019
 */

public class Wind {

    @SerializedName("speed")
    @Expose
    private float speed;
    @SerializedName("deg")
    @Expose
    private float deg;

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getDeg() {
        return deg;
    }

    public void setDeg(float deg) {
        this.deg = deg;
    }

}

package io.github.vishalecho.android.forecast.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author vishal kumar
 * @version 1.0
 * @since 06.01.2019
 */

public class Rain {

    @SerializedName("3h")
    @Expose
    private float _3h;

    public float get3h() {
        return _3h;
    }

    public void set3h(float _3h) {
        this._3h = _3h;
    }

}

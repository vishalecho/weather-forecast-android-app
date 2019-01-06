package io.github.vishalecho.android.forecast.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author vishal kumar
 * @version 1.0
 * @since 06.01.2019
 */

public class Sys {

    @SerializedName("pod")
    @Expose
    private String pod;

    public String getPod() {
        return pod;
    }

    public void setPod(String pod) {
        this.pod = pod;
    }

}

package io.github.vishalecho.android.forecast.utilities;

/**
 * @author vishal kumar
 * @version 1.0
 * @since 06.01.2019
 */
public class Utility {
    private static final String TAG = Utility.class.getSimpleName();

    public static Apis getApis() {
        return Client.getClient().create(Apis.class);
    }
}

package io.github.vishalecho.android.forecast.utilities;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author vishal kumar
 * @version 1.0
 * @since 06.01.2019
 * <p>
 * Client class build the retrofit client instance and accessible to project to send out network requests to an api.
 */
public class Client {
    private static Retrofit client = null;

    public static Retrofit getClient() {
        if (client == null) {
            client = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return client;
    }
}

package io.github.vishalecho.android.forecast.utilities;

import io.github.vishalecho.android.forecast.models.Forecast;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author vishal kumar
 * @version 1.0
 * @since 06.01.2019
 * <p>
 * Apis class is listed with All the Open Whether Map 5 day's weather forecast api end points
 */
public interface Apis {

    /**
     * By City Name
     */
    @GET("forecast")
    Call<Forecast> getWeatherForecastData(@Query("q") StringBuilder cityName, @Query("APPID") String APIKEY, @Query("units") String TempUnit);

}

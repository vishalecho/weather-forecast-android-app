package io.github.vishalecho.android.forecast.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import io.github.vishalecho.android.forecast.R;
import io.github.vishalecho.android.forecast.adapters.CardAdapter;
import io.github.vishalecho.android.forecast.models.DataObject;
import io.github.vishalecho.android.forecast.models.Forecast;
import io.github.vishalecho.android.forecast.utilities.Constants;
import io.github.vishalecho.android.forecast.utilities.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author vishal kumar
 * @version 1.0
 * @since 06.01.2019
 * <p>
 * Forecast Activity is an controller class to bind weather api data with model and show awesome view.
 */
public class ForecastActivity extends AppCompatActivity {
    private static final String TAG = ForecastActivity.class.getSimpleName();

    LocationManager locationManager;
    ProgressBar progressBar;
    ProgressDialog progressDialog;
    Location location;
    double latitute, longitude;
    Geocoder geocoder;
    StringBuilder addressStringBuilder;

    List<DataObject> weatherList;
    List<List<DataObject>> daysList;

    List<String> days;
    Set<String> distinctDays;
    CardAdapter cardAdapter;
    CardAdapter cardAdapter2;
    CardAdapter cardAdapter3;
    RecyclerView recyclerViewToday;
    RecyclerView recyclerViewTomorrow;
    RecyclerView recyclerViewLater;
    TabHost host;

    Toolbar toolbar;
    ImageView imageViewWeatherIcon;

    TextView tvTodayTemperature, tvTodayDescription, tvTodayWind, tvTodayPressure, tvTodayHumidity;
    ConstraintLayout layout;

    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        Constants.TEMP_UNIT = " " + getResources().getString(R.string.temp_unit);
        initMember();
        initUi();
        detectLocation();
        host = (TabHost) findViewById(R.id.tabHostT);
        host.setup();

        Date date = new Date();
        int day = date.getDay();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Today");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Today");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tomorrow");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Tomorrow");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Later");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Later");
        host.addTab(spec);
        recyclerViewToday = findViewById(R.id.my_recycler_view);
        recyclerViewTomorrow = findViewById(R.id.my_recycler_view2);
        recyclerViewLater = findViewById(R.id.my_recycler_view3);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this);
        recyclerViewToday.setLayoutManager(layoutManager);
        recyclerViewToday.setItemAnimator(new DefaultItemAnimator());

        recyclerViewTomorrow.setLayoutManager(layoutManager2);
        recyclerViewTomorrow.setItemAnimator(new DefaultItemAnimator());

        recyclerViewLater.setLayoutManager(layoutManager3);
        recyclerViewLater.setItemAnimator(new DefaultItemAnimator());

        getWeather(addressStringBuilder);
    }

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()) super.onBackPressed();
        else Toast.makeText(this, "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * This method gets the device current location to call the weather api by default city
     */
    private void detectLocation() {
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitute = location.getLatitude();
                longitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Toast.makeText(ForecastActivity.this, "Connect to network", Toast.LENGTH_SHORT).show();
            }
        };

        int permissionCheck = ContextCompat.checkSelfPermission(ForecastActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == 0) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        if (location != null) {
            latitute = location.getLatitude();
            longitude = location.getLongitude();
        }

        try {
            geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addressList = geocoder.getFromLocation(latitute, longitude, 1);
            addressStringBuilder = new StringBuilder();
            if (addressList.size() > 0) {
                Address locationAddress = addressList.get(0);
                for (int i = 0; i <= locationAddress.getMaxAddressLineIndex(); i++) {
                    locationAddress.getAddressLine(i);
                    /*remove comment if you subLocality need to be shown*/
                    // addressStringBuilder.append(locationAddress.getSubLocality()).append(",");
                    addressStringBuilder.append(locationAddress.getLocality());
                }
                /*TODO Set the current location to display*/
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                searchByCityName();
                return true;

            case R.id.action_about:
                showAboutDialog();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * This method shows a dialog to enter the city name to search
     */
    private void searchByCityName() {
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(this);
        alert.setTitle(this.getString(R.string.search_title));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setMaxLines(1);
        input.setSingleLine(true);
        alert.setView(input);
        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String result = input.getText().toString();
                if (!result.isEmpty()) {
                    fetchUpdateOnSearched(result);
                }
            }
        });
        alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancelled
            }
        });
        alert.show();
    }

    /**
     * This method call the getWeather api to searched the weather
     *
     * @param cityName
     */
    private void fetchUpdateOnSearched(String cityName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(cityName);
        getWeather(stringBuilder);
    }

    /**
     * This method shows an about detail of the app.
     */
    private void showAboutDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.app_name);

        final WebView webView = new WebView(this);
        String about = "<p>A opensource weather app.</p>" +
                "<p>Developed by <a href='mailto:vishal.iert.cs@gmail.com'>Vishal Kumar</a></p> <a href='https://github.com/vishalecho'> Follow on Github</a>" +
                "<p>Data provided by <a href='https://openweathermap.org/'>OpenWeatherMap</a>, under the <a href='http://creativecommons.org/licenses/by-sa/2.0/'>Creative Commons license</a>";
        TypedArray ta = obtainStyledAttributes(new int[]{android.R.attr.textColorPrimary, R.attr.colorAccent});
        String textColor = String.format("#%06X", (0xFFFFFF & ta.getColor(0, Color.BLACK)));
        String accentColor = String.format("#%06X", (0xFFFFFF & ta.getColor(1, Color.BLUE)));
        ta.recycle();
        about = "<style media=\"screen\" type=\"text/css\">" +
                "body {\n" +
                "    color:" + textColor + ";\n" +
                "}\n" +
                "a:link {color:" + accentColor + "}\n" +
                "</style>" +
                about;
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.loadData(about, "text/html", "UTF-8");
        alert.setView(webView);
        alert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        alert.show();
    }

    /**
     * This method get the Date
     *
     * @param milliTime
     * @return
     */
    private String getDate(Long milliTime) {
        Date currentDate = new Date(milliTime);
        SimpleDateFormat df = new SimpleDateFormat("dd");
        String date = df.format(currentDate);
        return date;
    }

    /**
     * This method call the openwheathermap api by city name and onResponseSuccess bind the data
     * with associated model and set the data to show awesome view to user.
     *
     * @param addressStringBuilder
     */
    private void getWeather(StringBuilder addressStringBuilder) {
        progressDialog.show();
        Call<Forecast> call = Utility.getApis().getWeatherForecastData(addressStringBuilder, Constants.API_KEY, Constants.UNITS);
        call.enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Log.i(TAG, "onResponse: " + response.isSuccessful());
                    weatherList = response.body().getDataObjectList();
                    distinctDays = new LinkedHashSet<>();
                    for (DataObject obj : weatherList) {
                        distinctDays.add(getDate(obj.getDt() * 1000));
                    }
                    Log.i("DISTINCTSIZE", distinctDays.size() + "");

                    days = new ArrayList<>();
                    days.addAll(distinctDays);

                    for (String day : days) {
                        List<DataObject> temp = new ArrayList<>();
                        Log.i("DAY", day);
                        for (DataObject data : weatherList) {
                            Log.i("ELEMENT", getDate(data.getDt() * 1000));
                            if (getDate(data.getDt() * 1000).equals(day)) {
                                Log.i("ADDEDDD", getDate(data.getDt() * 1000));
                                temp.add(data);
                            }
                        }
                        daysList.add(temp);
                    }

                    daysList.get(0).remove(0);

                    Log.i("DAYSLISTSIZE", daysList.size() + "");
                    cardAdapter = new CardAdapter(daysList.get(0));
                    recyclerViewToday.setAdapter(cardAdapter);

                    cardAdapter2 = new CardAdapter(daysList.get(1));
                    recyclerViewTomorrow.setAdapter(cardAdapter2);

                    cardAdapter3 = new CardAdapter(daysList.get(daysList.size() - 1));
                    recyclerViewLater.setAdapter(cardAdapter3);

                    toolbar.setTitle(response.body().getCity().getName() + ", " + response.body().getCity().getCountry());
                    switch (weatherList.get(0).getWeather().get(0).getIcon()) {
                        case "01d":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_clear_sky);
                            toolbar.setBackgroundResource(R.color.color_clear_and_sunny);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_clear_and_sunny));
                            break;
                        case "01n":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_clear_sky);
                            toolbar.setBackgroundResource(R.color.color_clear_and_sunny);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_clear_and_sunny));
                            break;
                        case "02d":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_few_cloud);
                            toolbar.setBackgroundResource(R.color.color_partly_cloudy);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_partly_cloudy));
                            break;
                        case "02n":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_few_cloud);
                            toolbar.setBackgroundResource(R.color.color_partly_cloudy);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_partly_cloudy));
                            break;
                        case "03d":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_scattered_clouds);
                            toolbar.setBackgroundResource(R.color.color_gusty_winds);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_gusty_winds));
                            break;
                        case "03n":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_scattered_clouds);
                            toolbar.setBackgroundResource(R.color.color_gusty_winds);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_gusty_winds));
                            break;
                        case "04d":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_broken_clouds);
                            toolbar.setBackgroundResource(R.color.color_cloudy_overnight);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_cloudy_overnight));
                            break;
                        case "04n":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_broken_clouds);
                            toolbar.setBackgroundResource(R.color.color_cloudy_overnight);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_cloudy_overnight));
                            break;
                        case "09d":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_shower_rain);
                            toolbar.setBackgroundResource(R.color.color_hail_stroms);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_hail_stroms));
                            break;
                        case "09n":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_shower_rain);
                            toolbar.setBackgroundResource(R.color.color_hail_stroms);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_hail_stroms));
                            break;
                        case "10d":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_rain);
                            toolbar.setBackgroundResource(R.color.color_heavy_rain);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_heavy_rain));
                            break;
                        case "10n":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_rain);
                            toolbar.setBackgroundResource(R.color.color_heavy_rain);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_heavy_rain));
                            break;
                        case "11d":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_thunderstorm);
                            toolbar.setBackgroundResource(R.color.color_thunderstroms);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_thunderstroms));
                            break;
                        case "11n":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_thunderstorm);
                            toolbar.setBackgroundResource(R.color.color_thunderstroms);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_thunderstroms));
                            break;
                        case "13d":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_snow);
                            toolbar.setBackgroundResource(R.color.color_snow);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_snow));
                            break;
                        case "13n":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_snow);
                            toolbar.setBackgroundResource(R.color.color_snow);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_snow));
                            break;
                        case "15d":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_mist);
                            toolbar.setBackgroundResource(R.color.color_mix_snow_and_rain);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_mix_snow_and_rain));
                            break;
                        case "15n":
                            imageViewWeatherIcon.setImageResource(R.drawable.ic_weather_mist);
                            toolbar.setBackgroundResource(R.color.color_mix_snow_and_rain);
                            layout.setBackgroundColor(getResources().getColor(R.color.color_mix_snow_and_rain));
                            break;
                    }
                    tvTodayTemperature.setText(weatherList.get(0).getMain().getTemp() + " " + getString(R.string.temp_unit));
                    tvTodayDescription.setText(weatherList.get(0).getWeather().get(0).getDescription());
                    tvTodayWind.setText(getString(R.string.wind_lable) + " " + weatherList.get(0).getWind().getSpeed() + " " + getString(R.string.wind_unit));
                    tvTodayPressure.setText(getString(R.string.pressure_lable) + " " + weatherList.get(0).getMain().getPressure() + " " + getString(R.string.pressure_unit));
                    tvTodayHumidity.setText(getString(R.string.humidity_lable) + " " + weatherList.get(0).getMain().getHumidity() + " " + getString(R.string.humidity_unit));
                }
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {
                progressDialog.dismiss();
                Log.e(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(ForecastActivity.this, getString(R.string.msg_failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method initialize the all ui member variables
     */
    private void initUi() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.progress));
        toolbar = findViewById(R.id.app_toolbar);
        setSupportActionBar(toolbar);
        imageViewWeatherIcon = findViewById(R.id.imageViewWeather);
        tvTodayTemperature = findViewById(R.id.todayTemperature);
        tvTodayDescription = findViewById(R.id.todayDescription);
        tvTodayWind = findViewById(R.id.todayWind);
        tvTodayPressure = findViewById(R.id.todayPressure);
        tvTodayHumidity = findViewById(R.id.todayHumidity);

        layout = findViewById(R.id.layoutWeather);
    }

    /**
     * This method initialize the all member variables
     */
    private void initMember() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        weatherList = new ArrayList<>();
        daysList = new ArrayList<>();
    }
}

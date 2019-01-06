package io.github.vishalecho.android.forecast.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import io.github.vishalecho.android.forecast.R;

/**
 * @author vishal kumar
 * @version 1.0
 * @since 06.01.2019
 *
 * SplashActivity is an controller class which detect the device location and ask for permission.
 */
public class SplashActivity extends AppCompatActivity {

    static final int PERMISSION_ACCESS_FINE_LOCATION = 0;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    LocationManager locationManager;
    AlertDialog.Builder builder;
    boolean isConnected;
    int permissionRequestResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initMembers();
        checkPermissionAndConnectivity(permissionRequestResult, locationManager, isConnected);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initMembers();
        checkPermissionAndConnectivity(permissionRequestResult, locationManager, isConnected);
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
     * This method request for permission and check the connectivity.
     * @param permissionRequestResult
     * @param locationManager
     * @param isConnected
     */
    private void checkPermissionAndConnectivity(int permissionRequestResult, LocationManager locationManager, boolean isConnected) {
        if (permissionRequestResult == 0) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (isConnected) {
                    startWheatherActivity();
                } else {
                    builder.setMessage(R.string.msg_internet).setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            } else {
                builder.setMessage(R.string.msg_gps).setCancelable(false)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        } else {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initMembers();
                    checkPermissionAndConnectivity(permissionRequestResult, locationManager, isConnected);
                } else {
                    builder.setMessage(R.string.msg_location_services).setCancelable(false)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        }
    }

    /**
     * This method start the next activity.
     */
    private void startWheatherActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, ForecastActivity.class));
                finish();
            }
        }, 1000);
    }

    /**
     * This method initialize the all member variables
     */
    private void initMembers() {
        builder = new AlertDialog.Builder(SplashActivity.this);
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        permissionRequestResult = this.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION");
    }

}

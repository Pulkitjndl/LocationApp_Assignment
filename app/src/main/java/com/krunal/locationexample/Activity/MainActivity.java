package com.krunal.locationexample.Activity;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.krunal.locationexample.Database.LogsEntity;
import com.krunal.locationexample.Database.Repository;
import com.krunal.locationexample.Service.GPSTracker;
import com.krunal.locationexample.Service.LocationWorker;
import com.krunal.locationexample.Utility.ClsCheckLocation;

import com.krunal.locationexample.R;
import com.krunal.locationexample.Utility.ClsGlobal;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.krunal.locationexample.Utility.ClsGlobal.getCurruntDateTime;
import static com.krunal.locationexample.Utility.ClsGlobal.getEntryDateFormat;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private Button check, Logs,stop;
    private final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stop = findViewById(R.id.stop);
        textView = findViewById(R.id.textView);
        check = findViewById(R.id.check);
        Logs = findViewById(R.id.Logs);

        requestLocationPermission();

        Log.e("Hour", String.valueOf(ClsGlobal.getCurrentHour()));

        int getCurrentHour = ClsGlobal.getCurrentHour();


        Log.e("isWorkScheduled",
                String.valueOf(ClsGlobal.isWorkScheduled(ClsGlobal.packageName.concat(".Location"))));


        check.setOnClickListener(v -> {

            // To Check if GPS is on or not.
            if (CheckGpsStatus()) {
                check.setText("Start");
                ClsCheckLocation.checkLocationServiceNew(MainActivity.this);
                Toast.makeText(getBaseContext(),"Fetching the data",Toast.LENGTH_LONG).show();
            }else{
                check.setText("Stop");

            }

            ClsGlobal.ScheduleWorker(ClsGlobal.packageName.concat(".Location.1Hour"),1);
        });
        stop.setOnClickListener(v ->{
            stop.setText("Stop");
            Toast.makeText(getBaseContext(),"fetching has stopped",Toast.LENGTH_LONG).show();
            ClsCheckLocation.checkLocationServiceNew(MainActivity.this);


        });
        Logs.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LogsActivity.class);
            startActivity(intent);
        });


    }



    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
    public void requestLocationPermission() {
        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
        }
    }


    public boolean CheckGpsStatus() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


}

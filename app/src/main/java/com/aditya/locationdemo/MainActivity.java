package com.aditya.locationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
// <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/> //gives you precise location using the gps
    //<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/> //gives you general location using wifi and cell tower

    //holding current location
    double latitude_val;
    double longitude_val;

    TextView latitude;
    TextView longitude;
    LocationListener locationListener;
    LocationManager locationManager;

    //this method tells us if some app is requesting permission and tracks whether the user has said yes or no
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
              //first we will check if we got permission or not
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //if the user gave the permission so in this if block we will update and display the location of the device
                //it will update the location of the device
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitude = (TextView)findViewById(R.id.latitude);
        longitude = (TextView)findViewById(R.id.longitude);

        Button button = (Button)findViewById(R.id.button);
        //setting up onClickListener on the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sending latitude and longitude data from main activity to the mapsActivity
                // TODO Auto-generated method stub
                double passingLatitude = latitude_val;
                double passingLongitude = longitude_val;
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                Bundle b = new Bundle();
                b.putDouble("Latitude", passingLatitude); //putInt if value passed is integer puString if value passed is String
                b.putDouble("Longitude", passingLongitude); //sending longitude data
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        //creating a LocationManager that will manage all our location tracking
        //getSystemService(Context.LOCATION_SERVICE) will access the location service of the android os of the device
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //LocationListener will give us updates about changes in the devices location in the application maps output
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //this function will handel all the updates that we are getting from the gps
                Log.i("Location",location.toString());
                //this will update those information onto the textView
                //typecasting latitude to integer and the converting integer to string
                //typecasting integer to string is necessary as the textView can only store values in string and does not accept integer values
                String Latitude = "Latitude";
                String Longitude = "Longitude";
                latitude.setText(Double.toString((double) location.getLatitude())+ " " +Latitude);
                longitude.setText(Double.toString((double) location.getLongitude())+ " " +Longitude);
                latitude_val = (double) location.getLatitude();
                longitude_val = (double) location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
              //this function will handel all the user inputs
            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        //checking if the user gave permission to use the devices location or not
        //if(ContextCompat.checkSelfPermission(this,what sort of permission tha we are looking for in the maifest.xml file)!= PackageManager.PERMISSION_GRANTED)
        //we are checking if the user have already given us permission to use the device's location or not
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //if the user didnt give the permission so in this if block we want to ask the permission from the user
            //ActivityCompat.requestPermissions(this, new String[]{what sort of permission tha we are asking for in the maifest.xml file},request code can be used to keep track of the number request that your application might ask from the user);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else{
            //if the user has already given his permission to use the location on his device then the code below will execute
            //it will update the location of the device
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            //here we will write the code to display the last known location of the user's device
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
           latitude_val = lastKnownLocation.getLatitude();
            longitude_val = lastKnownLocation.getLongitude();
        }
    }
}
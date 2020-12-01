package com.aditya.locationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/*
in order to remove the taskbar that shows battery percentage write the codes below in styles.xml
<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
    <!-- Customize your theme here. -->
    <item name="colorPrimary">@color/orange</item>
    <item name="colorPrimaryDark">@android:color/holo_orange_dark</item>
    <item name="android:windowNoTitle">true</item>
    <item name="android:windowFullscreen">true</item>
</style>

in order to remove action bar that shows application title
write these codes in android manifest xml file
 android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="Hiking navigator"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.AppCompat.NoActionBar">
 */

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

    //textviews holding the address of the current location
    TextView textViewState;
    TextView textViewCity;
    TextView textViewCountry;
    TextView textViewPostal_Code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitude = (TextView)findViewById(R.id.latitude);
        longitude = (TextView)findViewById(R.id.longitude);

        //texViews holding the address of the current location
        textViewState = (TextView)findViewById(R.id.state);
        textViewCity = (TextView)findViewById(R.id.city);
        textViewCountry = (TextView)findViewById(R.id.country);
        textViewPostal_Code = (TextView)findViewById(R.id.postal_code);
        TextView DevInfo = (TextView)findViewById(R.id.DevInfo);

        //adding button that will openListView activity of saved location
        Button showSavedLocation = (Button)findViewById(R.id.showSavedLocation);
        //setting up the showSavedLocation button to open up the activity holding saved location
        showSavedLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SavedLocationActivity3.class);
                startActivity(intent);
            }
        });

        //setting up DevInfo button to open a new activity
        DevInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DevActivity.class);
                startActivity(intent);
            }
        });

        Button button = (Button)findViewById(R.id.button);
        //setting up onClickListener on the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //sending latitude and longitude data from main activity to the mapsActivity using Intent
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

                //getting address from coordinates from the satellite
                //using geocoder allows us to pass any latitude and longitude and in return it gives us the address of that coordinate back to us
                //Locale.getDefault() will return the address of the current location of the device
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                //getting information from a particular location coordinates
                //maxresult: 1 will give us the best matched address for our current location
                try {
                    List<Address> listAddresses= geocoder.getFromLocation(latitude_val,longitude_val,1); //it is going to return a list of addresses

                    //checking if the listAddresses is null or not
                    if(listAddresses!=null && listAddresses.size()>0)
                    {
                        Log.i("place",listAddresses.get(0).toString());
                        //storing the address that we got from geocoder into the string
                        /* here when you get address from geo coder back from google server
                           admin  = state
                           locality = city

                         */
                        if(listAddresses.get(0).getAdminArea()!=null)
                        {
                           textViewState.setText(listAddresses.get(0).getAdminArea());
                        }
                        if(listAddresses.get(0).getLocality()!=null)
                        {
                            textViewCity.setText(listAddresses.get(0).getLocality());
                        }
                        if(listAddresses.get(0).getCountryName()!=null)
                        {
                            textViewCountry.setText(listAddresses.get(0).getCountryName());
                        }
                        if(listAddresses.get(0).getAddressLine(0)!=null)
                        {
                            textViewPostal_Code.setText(listAddresses.get(0).getAddressLine(0));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 30, locationListener);
            //here we will write the code to display the last known location of the user's device
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
           latitude_val = lastKnownLocation.getLatitude();
            longitude_val = lastKnownLocation.getLongitude();
        }
    }
}
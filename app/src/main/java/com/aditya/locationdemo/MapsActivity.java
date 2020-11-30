package com.aditya.locationdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    double receivingLongitude;
    double receivingLatitude;

    //** the code will required if you pass the location from the mapActivity but here we are passing the location information
    //**through the mainActivity so we wont be needing all this code

    /**
    //creating a LocationManager that will manage all our location tracking
    //getSystemService(Context.LOCATION_SERVICE) will access the location service of the android os of the device
    LocationManager locationManager;
    //LocationListener will give us updates about changes in the devices location in the application maps output
    LocationListener locationListener;
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        
        //getting texView latitude data from our mainActivity
        Bundle b = getIntent().getExtras();
        receivingLatitude = b.getDouble("Latitude");
        receivingLongitude = b.getDouble("Longitude");
        //converting integer value to String as log only accepts String values
        Log.i("recieveLatitude", Double.toString(receivingLatitude));
        Log.i("receivingLongitude", Double.toString(receivingLongitude));

        /**
        //creating a LocationManager that will manage all our location tracking
        //getSystemService(Context.LOCATION_SERVICE) will access the location service of the android os of the device
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        //LocationListener will give us updates about changes in the devices location in the application maps output
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //this function will handel all the updates that we are getting from the gps
                Log.i("Location",location.toString());
                //this will update those location information onto the map

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
        }
         **/
    }

    /**
    //this method tells us if some app is requesting permission and tracks whether the user has said yes or no
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //first we will check if we got permission or not
        //request code keep tracks of all the requests that our app may ask from the user
        if(requestCode==1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    //if the user gave the permission so in this if block we will update and display the location of the device
                    //it will update the location of the device
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            }
        }
    }
    **/

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //clears the map before marking the current location
        mMap.clear();
        // Add a marker in Sydney and move the camera
        LatLng userLocation = new LatLng(receivingLatitude, receivingLongitude); //now we can pass those latitude and longitude that the mainActivity sent us in this MapsActivity
        mMap.addMarker(new MarkerOptions().position(userLocation).title("User location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15));
        //newLatLngZoom(userLocation,map camera zoom level)

    }
}
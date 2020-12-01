package com.aditya.locationdemo;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

public class MapsActivitySavingLocation extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {
    //OnMapReadyCallback handles the functions of map appropriately
//after OnMapReadyCallback, GoogleMap.OnMapLongClickListener  add the following code that will handle the long taps on the mapActivity
    //note after that you will need to implement the correct methods

    //setting up location manager and location listener
    //so that we can open the saved location from our lisView on the map
    LocationManager locationManager;
    LocationListener locationListener;

    private GoogleMap mMap;

    //for displaying address above the marker
    String Add ="";

    public void centerMapOnLocation(Location location, String title) {
        if (location != null) {
            LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(userLocation).title(title));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
        }
    }

    //requesting for permission from the user
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centerMapOnLocation(lastKnownLocation, "Your Location");
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_saving_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

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

        //setting up the OnLongClickListener on the mapActivity , this here refers to this activity
        mMap.setOnMapLongClickListener(this);
//getting saved location address from the lisView
        Intent intent = getIntent();
        if (intent.getIntExtra("positionListViewItem", 0) == 0) {
            //creating a LocationManager that will manage all our location tracking
            //getSystemService(Context.LOCATION_SERVICE) will access the location service of the android os of the device
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            //LocationListener will give us updates about changes in the devices location in the application maps output
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    centerMapOnLocation(location, "Your Location");
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            //if user has granted the permission then go ahead the light is green
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                centerMapOnLocation(lastKnownLocation, "Your Location");
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
        else{
            //opening saved location on the map from the ListView
            Location openSavedLocation = new Location(LocationManager.GPS_PROVIDER);
            //SavedLocationActivity3.Savedplaces.get(intent.getIntExtra("positionListViewItem",0)) getting the latitude and Longitude stored in the savedPlaces ArrayList
            //SavedLocationActivity3.Savedplaces.get(intent.getIntExtra("positionListViewItem",0)).latitude telling it that we only need latitude
            openSavedLocation.setLatitude(SavedLocationActivity3.Savedplaces.get(intent.getIntExtra("positionListViewItem",0)).latitude);
            //this will fetch the longitude the code is the same
            openSavedLocation.setLongitude(SavedLocationActivity3.Savedplaces.get(intent.getIntExtra("positionListViewItem",0)).longitude);
            centerMapOnLocation(openSavedLocation, SavedLocationActivity3.places.get(intent.getIntExtra("positionListViewItem",0)));
    }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        //getting address of the user marked location
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        //for saving the full address for the lisView
        String savedAddress = "";
        try{
            List<Address> addressSaved = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            //checking if the addressSaved is null or not
            if(addressSaved!=null && addressSaved.size()>0)
            {
                Log.i("place",addressSaved.get(0).toString());
                //storing the address that we got from geocoder into the string
                        /* here when you get address from geo coder back from google server
                           admin  = state
                           locality = city

                         */
                if(addressSaved.get(0).getAdminArea()!=null)
                {
                    //for saving the full address for the lisView
                    savedAddress = addressSaved.get(0).getAdminArea() + "\n";
                }
                if(addressSaved.get(0).getLocality()!=null)
                {
                    //for saving the full address for the lisView
                    savedAddress += addressSaved.get(0).getLocality() + "\n";
                }
                if(addressSaved.get(0).getCountryName()!=null)
                {
                    //for saving the full address for the lisView
                    savedAddress += addressSaved.get(0).getCountryName() + "\n";
                }
                if(addressSaved.get(0).getAddressLine(0)!=null)
                {
                    //for saving the full address for the lisView
                    savedAddress = addressSaved.get(0).getAddressLine(0);
                    //for displaying address above the marker
                    Add = addressSaved.get(0).getAddressLine(0);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        //it handles the onLongClick Listener on this Activity and displays the address on the marker
        try {
            mMap.addMarker(new MarkerOptions().position(latLng).title(Add));
        }catch (Exception e){

        }

        //adding the data to the list in the SavedLocationActivity3.class
        //storing addresses
        SavedLocationActivity3.places.add(savedAddress); //stores the complete address onto the lisView array
        //storing latitude and longitude
        SavedLocationActivity3.Savedplaces.add(latLng);

            SavedLocationActivity3.arrayAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Location saved",Toast.LENGTH_SHORT).show();
    }
}
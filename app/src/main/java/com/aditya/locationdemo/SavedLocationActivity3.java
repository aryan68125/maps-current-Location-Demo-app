package com.aditya.locationdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class SavedLocationActivity3 extends AppCompatActivity {

    //ArrayList will store all the saved location that will later be displayed by the listView (address)
    static ArrayList<String> places= new ArrayList<String>();
    /*
        in order to send data onto the next Activity we need to define the list as a static
         */
    //creating arrayList for storing the marked locations on the map (latitude and Longitude)
    static ArrayList<LatLng> Savedplaces = new ArrayList<LatLng>();

    //defining ArrayAdapter
    static ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_location3);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.aditya.locationdemo", Context.MODE_PRIVATE);

        ArrayList<String> latitudes = new ArrayList<>(); //for storing latitudes
        ArrayList<String> longitudes = new ArrayList<>(); //for storing longitudes

        //clearing ArrayList Before getting the stored data and storing them inside the ArrayList
        places.clear();
        latitudes.clear();
        longitudes.clear();
        Savedplaces.clear();

        try {
           //getting stored data from the sharedPreferences and deserializing them and placing them into their respective arrayList
            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>())));
            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats",ObjectSerializer.serialize(new ArrayList<String>())));
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lons",ObjectSerializer.serialize(new ArrayList<String>())));


        } catch (Exception e) {
            e.printStackTrace();
        }

        //merging latitudes and longitudes into one Savedplaces LatLang Array list
        if (places.size() > 0 && latitudes.size() > 0 && longitudes.size() > 0) {
            if (places.size() == latitudes.size() && places.size() == longitudes.size()) {
                for (int i=0; i < latitudes.size(); i++) {
                    Savedplaces.add(new LatLng(Double.parseDouble(latitudes.get(i)), Double.parseDouble(longitudes.get(i))));
                }
            }
        } else {
            //if our application opens up for the first time and there is no data stored then it will display the default location
            places.add("Add a new place...");
            Savedplaces.add(new LatLng(0,0));
        }

       ListView ListViewSavedLocation = (ListView)findViewById(R.id.ListViewSavedLocation);

        //now we are setting up the places ArrayList to be shown via ArrayAdapter inside of the ListView
        arrayAdapter = new ArrayAdapter(this,R.layout.row,places);
        //connecting our ListView and Our ArrayAdapter
        ListViewSavedLocation.setAdapter(arrayAdapter);

        //setting up onclick listener on the listView
        ListViewSavedLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //send the location data inside the listView onto the mapActivity
                Intent intent = new Intent(SavedLocationActivity3.this,MapsActivitySavingLocation.class);
                intent.putExtra("positionListViewItem",position);
                startActivity(intent);
            }
        });
        Button GoBackToMainPage = (Button)findViewById(R.id.GoBackToMainPage);
        GoBackToMainPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
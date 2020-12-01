package com.aditya.locationdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    double receivingLongitude;
    double receivingLatitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_location3);
       ListView ListViewSavedLocation = (ListView)findViewById(R.id.ListViewSavedLocation);

        //now we are setting up the places ArrayList to be shown via ArrayAdapter inside of the ListView
        arrayAdapter = new ArrayAdapter(this,R.layout.row,places);
        //connecting our ListView and Our ArrayAdapter
        ListViewSavedLocation.setAdapter(arrayAdapter);
        //this condition will prevent current location from adding into the list multiple times
        if( places.size() == 0)
        {
            places.add("Current location");
            Savedplaces.add(new LatLng(0,0));
        }

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
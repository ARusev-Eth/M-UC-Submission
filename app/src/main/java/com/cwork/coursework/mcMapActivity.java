package com.cwork.coursework;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class mcMapActivity extends FragmentActivity {

    List<mcMapData> mapDataList;
    private Marker[] mapDataMarkerList = new Marker[5];
    private GoogleMap mapStarSigns;
    private float markerColours[] = {210.0f, 120.0f, 300.0f, 330.0f, 270.0f};
    private LatLng latlangEKCentre = new LatLng(55.867537,-4.250419); //Saltire Centre

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.map_layout);

        Log.e(null,"Map OnCreate Running!");
        mapDataList = new ArrayList<mcMapData>();
        mcMapDataDBMgr mapDB = new mcMapDataDBMgr(this,"mapEKFamous5.s3db", null, 1);
        Log.e(null,"Map activity DBMgr instantiated!");
        try{
            mapDB.dbCreate();
        }catch(IOException e){
            e.printStackTrace();
        }

        Log.e(null,"Map activity DB created!");

        mapDataList = mapDB.allMapData();

        Log.e(null,"Map activity filled array");
        SetUpMap();
        AddMarkers();
    }

    public void SetUpMap(){
        mapStarSigns = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();    //Create the map and apply to the variable
        if(mapStarSigns != null) {
            mapStarSigns.moveCamera(CameraUpdateFactory.newLatLngZoom(latlangEKCentre, 16));        //Set the default position of EK
            mapStarSigns.setMyLocationEnabled(true);                                                //Turn on GPS
            mapStarSigns.getUiSettings().setCompassEnabled(true);                                   //Turn on the Compass
            mapStarSigns.getUiSettings().setMyLocationButtonEnabled(true);                          //Turn on the Location Buttons Functionality
            mapStarSigns.getUiSettings().setRotateGesturesEnabled(true);
        }
    }

    public void AddMarkers(){
        MarkerOptions marker;
        mcMapData mapData;
        String mrkTitle;
        String mrkText;

        //For all marker options in dbList list
        for(int i = 0; i < mapDataList.size(); i++) {
            mapData = mapDataList.get(i);
            mrkTitle = mapData.getFirstname() + " " + mapData.getSurname() +
                    " Open Hours: " + mapData.getOccupation();
            mrkText = "Room Sign: " + mapData.getStarSign();
            marker = SetMarker(mrkTitle, mrkText, new LatLng(mapData.getLatitude(),
                    mapData.getLongitude()), markerColours[i],true);
            mapDataMarkerList[i] = mapStarSigns.addMarker(marker);
        }
    }

    public MarkerOptions SetMarker(String title, String snippet, LatLng positions, float markerColour, boolean centreAnchored) {
        float anchorX;
        float anchorY;
        MarkerOptions marker;

        if(centreAnchored){
            anchorX = 0.5f;
            anchorY = 0.5f;
        }else {
            anchorX = 0.5f;
            anchorY = 1.0f;
        }

        marker = new MarkerOptions().title(title).snippet(snippet).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)).anchor(anchorX, anchorY).position(positions);

        return marker;
    }
}

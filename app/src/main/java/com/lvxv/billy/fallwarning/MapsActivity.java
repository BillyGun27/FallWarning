package com.lvxv.billy.fallwarning;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    SharedPreferences sharedPref ;
    double Lat ;
    double Lng ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        sharedPref = getApplicationContext().getSharedPreferences( "Maps", Context.MODE_PRIVATE);

        String Hour = sharedPref.getString("Hour", "030508" );

        //  SharedPreferences sharedPref = getApplicationContext().getSharedPreferences( "Maps", Context.MODE_PRIVATE);
        long lLat = sharedPref.getLong("Lat", Double.doubleToRawLongBits(-7.284986) );
        long lLng = sharedPref.getLong("Lng", Double.doubleToRawLongBits(112.795926));

        Lat = Double.longBitsToDouble(lLat) ;
        Lng = Double.longBitsToDouble(lLng) ;

        TextView hh = findViewById(R.id.hour);
        //hh.setText(HourChar[0] + HourChar[1] + ":" + HourChar[2] + HourChar[3] + ":" + HourChar[4] + HourChar[5] + " = "+ Hour );
        hh.setText("Last Updated : "+ Hour.substring(0,2) +":"+ Hour.substring(2,4) +":"+ Hour.substring(4,6) );

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

        // Add a marker in Sydney and move the camera
        LatLng coordinate = new LatLng(Lat , Lng );
        mMap.addMarker(new MarkerOptions().position(coordinate).title("Your Family"));
        float zoomLevel = 16.0f; //This goes up to 21
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom(coordinate, zoomLevel) ) ;
    }
}

package com.lvxv.billy.fallwarning;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class WarningActivity extends AppCompatActivity {

    AlarmController  alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning);


        alarm = new AlarmController(this) ;

        alarm.playSound();

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences( "Maps", Context.MODE_PRIVATE);
        long lLat = sharedPref.getLong("Lat", Double.doubleToRawLongBits(-7.284986) );
        long lLng = sharedPref.getLong("Lng", Double.doubleToRawLongBits(112.795926));
        String Hour = sharedPref.getString("Hour", "030508" );

        double Lat = Double.longBitsToDouble(lLat) ;
        double Lng = Double.longBitsToDouble(lLng) ;

       // char[] HourChar = Hour.toCharArray();

        TextView tt = findViewById(R.id.LL);
        tt.setText(String.valueOf(Lat)+" "+String.valueOf(Lng));
        TextView hh = findViewById(R.id.hour);
        //hh.setText(HourChar[0] + HourChar[1] + ":" + HourChar[2] + HourChar[3] + ":" + HourChar[4] + HourChar[5] + " = "+ Hour );
        hh.setText(Hour.substring(0,2) +":"+ Hour.substring(2,4) +":"+ Hour.substring(4,6) );
    }

    public void OpenWarning(View view){
        alarm.stopSound();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }

    public void CloseWarning(View view){
        alarm.stopSound();
        finish();
    }


    @Override
    public void onBackPressed() {
        alarm.stopSound();
        finish();
    }


    @Override
    protected void onUserLeaveHint()
    {
        Log.d("onUserLeaveHint","Home button pressed");
        alarm.stopSound();
        super.onUserLeaveHint();
    }

}

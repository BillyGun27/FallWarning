package com.lvxv.billy.fallwarning;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class DevicePairingActivity extends AppCompatActivity {
    String TAG ="MultiSIM";

    /* GPS Constant Permission */
    private static final int MY_PERMISSION_ACCESS_COARSE_LOCATION = 11;
    private static final int MY_PERMISSION_ACCESS_FINE_LOCATION = 12;

    /* Position */
    private static final int MINIMUM_TIME = 10000;  // 10s
    private static final int MINIMUM_DISTANCE = 50; // 50m

    /* GPS */
    private String mProviderName;
    private LocationManager mLocationManager;
    private LocationListener mLocationListener;


    SharedPreferences sharedPref ;
    int SimVal;
    String phoneNum;
    TextView simmer;

    EditText phoneEdit ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_pairing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Get the best provider between gps, network and passive
        Criteria criteria = new Criteria();
        mProviderName = mLocationManager.getBestProvider(criteria, true);



        sharedPref = getApplicationContext().getSharedPreferences( "Maps", Context.MODE_PRIVATE);
        SimVal = sharedPref.getInt("Sim",0 );
        phoneNum = sharedPref.getString("phoneNum","none");

        simmer = findViewById(R.id.simmer);
        simmer.setText( "Sim Card "+Integer.toString(SimVal+1) );

        phoneEdit = findViewById(R.id.phoneNum);
        if(phoneNum != "none"){
            phoneEdit.setText( phoneNum );
        }

        //CheckSim();

    }




    public void SendMessage(View view){
        //SharedPreferences sharedPref = getApplicationContext().getSharedPreferences( "Maps", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("phoneNum", phoneEdit.getText().toString()  );

        editor.commit();

        SimVal = sharedPref.getInt("Sim",0 );
        phoneNum = sharedPref.getString("phoneNum","none");

        Toast.makeText(getApplicationContext(),phoneNum, Toast.LENGTH_LONG).show();

        // Display the SMS message in a Toast
        Toast.makeText(getApplicationContext(),"Sending...", Toast.LENGTH_LONG).show();
        SimUtil.sendSMS(this,SimVal,phoneNum ,null,"12345 SAVE",null,null);
        //0-1

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK );
        startActivity(intent);
    }

    public void ChooseSim(View view){
       AlertDialogSim ads = new AlertDialogSim();
       ads.show(getFragmentManager(),"SIM");

       //SharedPreferences sharedPref = getApplicationContext().getSharedPreferences( "Maps", Context.MODE_PRIVATE);

        //int SimVal = sharedPref.getInt("Sim",0 );

       // TextView simmer = findViewById(R.id.simmer);
       // simmer.setText( Integer.toString(SimVal) );

    }

    public void CheckSim(){

        TextView simmer = findViewById(R.id.simmer);

        // API 23: we have to check if ACCESS_FINE_LOCATION and/or ACCESS_COARSE_LOCATION permission are granted
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                TelephonyManager manager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
                if (manager.getPhoneCount() == 2) {
                    // Dual sim
                    Toast.makeText(this, "dual", Toast.LENGTH_SHORT).show();
                }
                simmer.setText("a");
            }else{
                TelephonyManager manager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);

// Get information about all radio modules on device board
// and check what you need by calling #getCellIdentity.

                final List<CellInfo> allCellInfo = manager.getAllCellInfo();
                for (CellInfo cellInfo : allCellInfo) {
                    if (cellInfo instanceof CellInfoGsm) {
                        Toast.makeText(this, "dualgsm", Toast.LENGTH_SHORT).show();
                        //CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                        //TODO Use cellIdentity to check MCC/MNC code, for instance.
                    } else if (cellInfo instanceof CellInfoWcdma) {
                        //CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
                        Toast.makeText(this, "dualwcdma", Toast.LENGTH_SHORT).show();
                    } else if (cellInfo instanceof CellInfoLte) {
                        //CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                        Toast.makeText(this, "duallte", Toast.LENGTH_SHORT).show();
                    } else if (cellInfo instanceof CellInfoCdma) {
                        //CellIdentityCdma cellIdentity = ((CellInfoCdma) cellInfo).getCellIdentity();
                        Toast.makeText(this, "dualcdma", Toast.LENGTH_SHORT).show();
                    }
                }

                //TelephonyManager manager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
                simmer.setText("b");
// Get information about all radio modules on device board
// and check what you need by calling #getCellIdentity.

                //int simval=0;
                /*
                final List<CellInfo> allCellInfo = manager.getAllCellInfo();
                for (CellInfo cellInfo : allCellInfo) {
                    if (cellInfo instanceof CellInfoGsm) {
                        //CellIdentityGsm cellIdentity = ((CellInfoGsm) cellInfo).getCellIdentity();
                        //TODO Use cellIdentity to check MCC/MNC code, for instance.
                  //      simval++;
                    } else if (cellInfo instanceof CellInfoWcdma) {
                        //CellIdentityWcdma cellIdentity = ((CellInfoWcdma) cellInfo).getCellIdentity();
                   //     simval++;
                    } else if (cellInfo instanceof CellInfoLte) {
                        //CellIdentityLte cellIdentity = ((CellInfoLte) cellInfo).getCellIdentity();
                   //     simval++;
                    } else if (cellInfo instanceof CellInfoCdma) {
                        //CellIdentityCdma cellIdentity = ((CellInfoCdma) cellInfo).getCellIdentity();
                    //    simval++;
                    }
                }*/


                //simmer.setText(simval);
            }

            // One or both permissions are denied.
        } else {

            // The ACCESS_COARSE_LOCATION is denied, then I request it and manage the result in
            // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_ACCESS_COARSE_LOCATION);
            }
            // The ACCESS_FINE_LOCATION is denied, then I request it and manage the result in
            // onRequestPermissionsResult() using the constant MY_PERMISSION_ACCESS_FINE_LOCATION
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this,
                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                        MY_PERMISSION_ACCESS_FINE_LOCATION);
            }

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                } else {
                    // permission denied
                }
                break;
            }

                case MY_PERMISSION_ACCESS_FINE_LOCATION: {
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // permission was granted
                    } else {
                        // permission denied
                    }
                    break;
                }

            }
        }



}

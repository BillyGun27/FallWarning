package com.lvxv.billy.fallwarning;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DeviceSettingActivity extends AppCompatActivity {

    private static DeviceSettingActivity activity;

    public static DeviceSettingActivity instance() {
        return activity;
    }

    SharedPreferences sharedPref ;
    int SimVal;
    String phoneNum;
    TextView simmer,delayVal;

    private ArrayList<String> phoneList = new ArrayList<String>();
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    TextView deviceCheck;
    /*String[] mobileArray = {"Android","IPhone","WindowsMobile","Blackberry",
            "WebOS","Ubuntu","Windows7","Max OS X"};*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = getApplicationContext().getSharedPreferences( "Maps", Context.MODE_PRIVATE);
        phoneNum = sharedPref.getString("phoneNum","none");

        simmer = findViewById(R.id.simmer);
        simmer.setText( "Sim Card "+Integer.toString(SimVal+1) );


        TextView device = findViewById(R.id.Device);

        if(phoneNum != "none"){
            device.setText("Connected Device : "+phoneNum);
        }

        //

        delayVal = findViewById(R.id.delayVal);
        delayVal.setText("--");

        phoneList.add("Press Check Device Setting");
        phoneList.add("To See Current Configuration");

        deviceCheck = findViewById(R.id.deviceCheck);
        mListView = (ListView) findViewById(R.id.regList);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, phoneList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(MyItemClickListener);
        //readSMS();

    }

    public void onStart() {
        super.onStart();

        activity = this;
    }

    //final String[] newPhone,
    public void updateList(final String[] newPhone) {
      ////  adapter.insert(newSms, 0);
       // adapter.clear();
        //adapter.addAll(newPhone);
       // phoneList.add("zero");
        phoneList.clear();

        for (int i = 0; i < newPhone.length; i++) {
           phoneList.add(newPhone[i]);
        }

                //newPhone;
        adapter.notifyDataSetChanged();


    }

    public void updateDelay(final String newDelay) {

        delayVal.setText(newDelay);
    }

    public void DelNum(final String phoneNumber, final int index){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to delete "+phoneNumber);
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText( getApplicationContext() ,"You deleted "+ phoneNumber ,Toast.LENGTH_LONG).show();
                                phoneList.remove(index);
                                smsSender("12345 CONFIG SET DELETE "+ phoneNumber  );
                            }
                        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark) );
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark) );

    }

    private AdapterView.OnItemClickListener MyItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            try {
                Toast.makeText(getApplicationContext(), adapter.getItem(pos), Toast.LENGTH_SHORT).show();
                DelNum( adapter.getItem(pos), pos );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public void smsSender(String msg ){
        SimVal = sharedPref.getInt("Sim",0 );
        phoneNum = sharedPref.getString("phoneNum","none");

        // Display the SMS message in a Toast
        Toast.makeText(getApplicationContext(),"Sending...", Toast.LENGTH_LONG).show();
        //SimVal ==> 0-1
        SimUtil.sendSMS(this,SimVal,phoneNum ,null,msg ,null,null);
    }

    public void SendMessageCheck(View view){

        deviceCheck.setText("Registered Phone Number");
        mListView.setVisibility(View.VISIBLE);
        //SharedPreferences sharedPref = getApplicationContext().getSharedPreferences( "Maps", Context.MODE_PRIVATE);

        smsSender("12345 CONFIG BROADCAST 1");


    }

    public void SendMessageCheckDelay(View view){

        smsSender("12345 CONFIG VIEW");


    }

    public void ChooseSim(View view){
        AlertDialogSim ads = new AlertDialogSim();
        ads.show(getFragmentManager(),"SIM");

        //SharedPreferences sharedPref = getApplicationContext().getSharedPreferences( "Maps", Context.MODE_PRIVATE);

        //int SimVal = sharedPref.getInt("Sim",0 );

        // TextView simmer = findViewById(R.id.simmer);
        // simmer.setText( Integer.toString(SimVal) );

    }

    public void AddNumber(View view){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        phoneList.add( userInputDialogEditText.getText().toString() );
                        adapter.notifyDataSetChanged();

                        Toast.makeText(getApplicationContext(),userInputDialogEditText.getText().toString() , Toast.LENGTH_LONG).show();
                        smsSender("12345 CONFIG SET ADD "+ userInputDialogEditText.getText().toString() );

                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        alertDialogAndroid.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark) );
        alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark) );
    }

    public void AddDelay(View view){
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this);
        View mView = layoutInflaterAndroid.inflate(R.layout.number_picker_dialog, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);

        final NumberPicker numpick =  mView.findViewById(R.id.numberPicker);
        numpick.setMaxValue(6000);
        numpick.setMinValue(0);
        numpick.setWrapSelectorWheel(true);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {
                        // ToDo get user input here
                        delayVal.setText(Integer.toString(numpick.getValue()));
                        Toast.makeText(getApplicationContext(), Integer.toString(numpick.getValue()) , Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),"Sending...", Toast.LENGTH_LONG).show();
                        smsSender("12345 CONFIG SET DELAY " + Integer.toString(numpick.getValue()) );

                    }
                })

                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
        alertDialogAndroid.show();
        alertDialogAndroid.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark) );
        alertDialogAndroid.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark) );
    }

}

package com.lvxv.billy.fallwarning;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //private static final String INBOX_URI = "content://sms/inbox";
    private static MainActivity activity;
    //private ArrayList<String> smsList = new ArrayList<String>();
    //private ListView mListView;
    //private ArrayAdapter<String> adapter;
    public static MainActivity instance() {
        return activity;
    }

    SharedPreferences sharedPref ;
    String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //mListView = (ListView) findViewById(R.id.list);
        //adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, smsList);
        //mListView.setAdapter(adapter);
        //mListView.setOnItemClickListener(MyItemClickListener);
        //readSMS();

        sharedPref = getApplicationContext().getSharedPreferences( "Maps", Context.MODE_PRIVATE);
        phoneNum = sharedPref.getString("phoneNum","none");

        TextView device = findViewById(R.id.Device);
        Button ViewFamily = findViewById(R.id.location);
        Button ConfigureDevice = findViewById(R.id.configure);

        if(phoneNum == "none"){
            ViewFamily.setEnabled(false);
            ConfigureDevice.setEnabled(false);
        }else {
            device.setText("Connected Device : "+phoneNum);
        }

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    NewMessageNotification mess ;
    @Override
    public void onStart() {
        super.onStart();

        // Making a request to url and getting response
      /*  String jsonStr = "{id:'xxx',name:'zzz'}";//sh.makeServiceCall(url);
        String id;

        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);

                id= jsonObj.getString("id");
            } catch (final JSONException e) {
                id= "Json parsing error: " + e.getMessage();

            }
        } else {

            id= "Couldn't get json from server. Check LogCat for possible errors!";

        }

*/
        //mess.notify(this,id,0);
        //AlarmController alarm = new AlarmController(this) ;
        //alarm.playSound();
        activity = this;
    }


    public void TesWarning(View view){
        Intent intent = new Intent(this, WarningActivity.class);
        startActivity(intent);

    }

    public void SendMessage(View view){
        Intent intent = new Intent(this, DevicePairingActivity.class);
        startActivity(intent);

    }

    public void DeviceSetting(View view){
        Intent intent = new Intent(this, DeviceSettingActivity.class);
        startActivity(intent);

    }

    public void CheckLocation(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }

/*
    public void readSMS() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxCursor = contentResolver.query(Uri.parse(INBOX_URI), null, null, null, null);
        int senderIndex = smsInboxCursor.getColumnIndex("address");
        int messageIndex = smsInboxCursor.getColumnIndex("body");
        if (messageIndex < 0 || !smsInboxCursor.moveToFirst()) return;
        adapter.clear();
        do {
            String sender = smsInboxCursor.getString(senderIndex);
            String message = smsInboxCursor.getString(messageIndex);
            String formattedText = String.format(getResources().getString(R.string.sms_message), sender, message);
            adapter.add(Html.fromHtml(formattedText).toString());
        } while (smsInboxCursor.moveToNext());
    }
    public void updateList(final String newSms) {
        adapter.insert(newSms, 0);
        adapter.notifyDataSetChanged();
    }
    private AdapterView.OnItemClickListener MyItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
            try {
                Toast.makeText(getApplicationContext(), adapter.getItem(pos), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    */



}

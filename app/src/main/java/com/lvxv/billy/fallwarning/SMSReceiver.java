package com.lvxv.billy.fallwarning;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by ASUS on 5/8/2018.
 */

public class SMSReceiver  extends BroadcastReceiver {

    // SmsManager class is responsible for all SMS related actions
    final SmsManager sms = SmsManager.getDefault();
    NewMessageNotification mess ;
    String id,status;
    Double Lat,Lng;
    String Hour;
    String DeviceDelay = "0" ,Message;
    String[] DeviceMember = {"0"};


    public void onReceive(Context context, Intent intent) {
        // Get the SMS message received
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                // A PDU is a "protocol data unit". This is the industrial standard for SMS message
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    // This will create an SmsMessage object from the received pdu
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    // Get sender phone number
                    String phoneNumber = sms.getDisplayOriginatingAddress();
                    String sender = phoneNumber;
                    String message = sms.getDisplayMessageBody();
                    String formattedText = String.format(context.getResources().getString(R.string.sms_message), sender, message);
                    // Display the SMS message in a Toast
                   // Toast.makeText(context, formattedText, Toast.LENGTH_LONG).show();

                    SharedPreferences sharedPref = context.getSharedPreferences( "Maps", Context.MODE_PRIVATE);
                    String SavedpohneNum =  sharedPref.getString("phoneNum","none");

                    id = phoneNumber;
                    JSONturner(message,context);

                    boolean checkPhone = PhoneNumberUtils.compare(context, SavedpohneNum, phoneNumber);
                    if(checkPhone){
                            if(status == "SettingDelay"){
                                Toast.makeText(context, status, Toast.LENGTH_LONG).show();
                                DeviceSettingActivity inst = DeviceSettingActivity.instance();
                                inst.updateDelay(DeviceDelay);
                            } else if(status == "SettingBroadcast"){
                                Toast.makeText(context, status, Toast.LENGTH_LONG).show();
                                DeviceSettingActivity inst = DeviceSettingActivity.instance();
                                inst.updateList(DeviceMember);
                            } else if(status == "Warning"){
                                //notification
                                mess.notify(context, status +" From "+sender ,0);

                                Intent intentone = new Intent(context.getApplicationContext(), WarningActivity.class);
                                intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intentone);
                            }else if(status == "Confirmation") {
                                Toast.makeText(context, Message, Toast.LENGTH_LONG).show();
                            }/*else {

                            boolean checkPhone = PhoneNumberUtils.compare(context, SavedpohneNum, phoneNumber);
                            if(checkPhone){
                                Toast.makeText(context, "true" +  phoneNumber, Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(context,  "not" + SavedpohneNum , Toast.LENGTH_LONG).show();
                            }

                         //   DeviceSettingActivity inst = DeviceSettingActivity.instance();
                          //  inst.updateList(DeviceDelay,DeviceMember);
                        }*/
                    }



                   // MainActivity inst = MainActivity.instance();
                   // inst.updateList(formattedText);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void JSONturner(String jsonStr , Context context){
        // Making a request to url and getting response
        //String jsonStr = "{id:'xxx',name:'zzz'}";//sh.makeServiceCall(url);


        if (jsonStr != null) {
            try {

                Object json = new JSONTokener(jsonStr).nextValue();
                if (json instanceof JSONObject){
                    //you have an object
                  //  status = "Object";

                    JSONObject jsonObj = new JSONObject(jsonStr);

                    if( jsonObj.has("message") ){

                        status = "Confirmation";
                        Message = jsonObj.getString("message");

                    }else if( jsonObj.has("delayGPS") ){
                        status = "SettingDelay";

                        DeviceDelay = jsonObj.getString("delayGPS");



                    }else if( jsonObj.has("lastLat") && jsonObj.has("lastLon") ){
                        status = "Warning";

                        //  id = jsonObj.getString("id");
                        Lat = Double.parseDouble( jsonObj.getString("lastLat") ) ;
                        Lng = Double.parseDouble( jsonObj.getString("lastLon") ) ; ;
                        Hour =  jsonObj.getString("lastHour")  ;

                        SaveMap(Lat , Lng ,context);

                    }


                }
                else if (json instanceof JSONArray){
                    //you have an array
                 //   status = "Array";
                        status = "SettingBroadcast";

                        JSONArray jsonArr =  new JSONArray(jsonStr); //jsonObj.getJSONArray("broadcast"); //

                        DeviceMember = new String[jsonArr.length()];
                        for (int i = 0; i < jsonArr.length(); i++) {
                            DeviceMember[i] = jsonArr.getString(i);
                        }


                }







            } catch (final JSONException e) {
                id= "Json parsing error: " + e.getMessage();

            }
        } else {

            id= "Couldn't get json from server. Check LogCat for possible errors!";

        }


    }

    void SaveMap(double Lat , double Lng ,Context context){
        /*
        * - Double to Long:
Double.doubleToRawLongBits(double);

- Long to Double
Double.longBitsToDouble(defaultLongValue);
        * */
        SharedPreferences sharedPref = context.getSharedPreferences( "Maps", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong("Lat", Double.doubleToRawLongBits(Lat) );
        editor.putLong("Lng", Double.doubleToRawLongBits(Lng) );
        editor.putString("Hour", Hour );
        editor.commit();


    }




}

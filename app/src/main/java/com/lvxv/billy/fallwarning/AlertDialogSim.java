package com.lvxv.billy.fallwarning;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ASUS on 5/28/2018.
 */

public class AlertDialogSim extends DialogFragment {

//    LayoutInflater inflater = getActivity().getLayoutInflater();
 //   final View view = inflater.inflate(R.layout.activity_device_pairing, null);

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] simCard = getResources().getStringArray(R.array.sim_card);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_title)
                .setItems(R.array.sim_card, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), simCard[which], Toast.LENGTH_SHORT).show();

                        SharedPreferences sharedPref =  getActivity().getSharedPreferences( "Maps", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("Sim", which  );

                        editor.commit();

                        int SimVal = sharedPref.getInt("Sim",0 );
                       TextView simmer = getActivity().findViewById(R.id.simmer);
                        simmer.setText( "Sim Card "+Integer.toString(SimVal+1) );

                    }
                });
        AlertDialog dialog = alertDialogBuilder.create();
        return dialog;
    }
}

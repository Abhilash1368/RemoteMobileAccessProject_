package main.com.project_p;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ABHILASH on 4/7/2017.
 */

public class Tab1 extends Fragment implements CompoundButton.OnCheckedChangeListener {
    FloatingActionButton fab;
    AlertDialog.Builder adb;
    String[] key_array={"message_switch_value","location_switch_value","silent_switch_value","contact_switch_value","calllog_switch_value","ringmyphone_switch_value","hide_switch_value"};;
    ArrayList<Switch> switch_array=new ArrayList<>();
    SharedPreferences sharedPreferences;SharedPreferences.Editor editor;
    final static String sp_key="my_sp_key";

    public Switch message_switch,location_switch,silent_switch,contact_switch,calllog_switch,ringmyphone_switch,hide_switch;
    public static Boolean message_switch_value,location_switch_value,silent_switch_value,contact_switch_value,calllog_switch_value;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_pin,container,false);

        sharedPreferences=getActivity().getApplicationContext().getSharedPreferences(sp_key,MODE_PRIVATE);
        editor=sharedPreferences.edit();

        message_switch= (Switch) view.findViewById(R.id.message_switch);
        message_switch.setOnCheckedChangeListener(this);
        location_switch= (Switch) view.findViewById(R.id.location_switch);
        location_switch.setOnCheckedChangeListener(this);
        silent_switch= (Switch) view.findViewById(R.id.silent_switch);
        silent_switch.setOnCheckedChangeListener(this);
        contact_switch= (Switch) view.findViewById(R.id.contact_switch);
        contact_switch.setOnCheckedChangeListener(this);
        calllog_switch= (Switch) view.findViewById(R.id.calllog_switch);
        calllog_switch.setOnCheckedChangeListener(this);
        ringmyphone_switch=(Switch)view.findViewById(R.id.ringmyphone_switch);
        ringmyphone_switch.setOnCheckedChangeListener(this);
        hide_switch= (Switch) view.findViewById(R.id.hide_switch);
        hide_switch.setOnCheckedChangeListener(this);
        switch_array.add(message_switch);switch_array.add(location_switch);switch_array.add(silent_switch);switch_array.add(contact_switch);switch_array.add(calllog_switch);
        switch_array.add(ringmyphone_switch);switch_array.add(hide_switch);
       //hide_switch.setChecked(false);
       // hide_switch.setEnabled(false);

        //adb.setCancelable(false);


          //if (sharedPreferences.getBoolean("permissions",false)){
        for (int i=0;i<key_array.length;i++){
            if(sharedPreferences.getBoolean(key_array[i],true)){
                switch_array.get(i).setChecked(true);}
            else{
                switch_array.get(i).setChecked(false);}}//}
        NativeExpressAdView adView = (NativeExpressAdView)view.findViewById(R.id.adView);

        AdRequest request = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(request);
//        p.requestPermissionS();
//
//        // Check if the notification policy access has been granted for the app.
//        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
//            adb = new AlertDialog.Builder(getActivity());
//            adb.setTitle("Permissions Required");
//            adb.setMessage("Please enable DND permission for this app in next screen to use silent/ring mode feature");
//            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//
////                if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
//                    Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
//                    startActivity(intent);
//                    //}
//                    dialog.dismiss();
//
//
//                } });adb.show();}

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.message_switch:
                if(b){

                    editor.putBoolean("message_switch_value",true);
                }

                else {
                    alert(message_switch,"message_switch_value");
                }
                break;
            case R.id.location_switch:
                if(b){
                    editor.putBoolean("location_switch_value",true);}
                else
                    alert(location_switch,"location_switch_value");
                break;
            case R.id.silent_switch:
                if(b){
//                    NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//
//                    // Check if the notification policy access has been granted for the app.
//                    if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
//                        Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
//                        startActivity(intent);
//                    }
                    editor.putBoolean("silent_switch_value",true);}
                else
                    alert(silent_switch,"silent_switch_value");
                break;
            case R.id.contact_switch:
                if(b)
                    editor.putBoolean("contact_switch_value",true);
                else
                    alert(contact_switch,"contact_switch_value");
                break;
            case R.id.calllog_switch:
                if(b)
                    editor.putBoolean("calllog_switch_value",true);
                else
                    alert(calllog_switch,"calllog_switch_value");
                break;
            case  R.id.ringmyphone_switch:
                if(b)
                    editor.putBoolean("ringmyphone_switch_value",true);
                else
                    alert(ringmyphone_switch,"ringmyphone_switch_value");
                break;
            case  R.id.hide_switch:
                if(b)
                    editor.putBoolean("hide_switch_value",true);
                else
                    alert(hide_switch,"hide_switch_value");
                break;
        } editor.commit();
    }
    public void alert(final Switch view_switch, final String key){
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setMessage("This service will be disabled");
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                editor.putBoolean(key,false);
                editor.commit();

            } });
        adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                view_switch.setChecked(true);
                editor.putBoolean(key,true);
                editor.commit();
            } });
        adb.setCancelable(false);
        adb.show();

    }

}

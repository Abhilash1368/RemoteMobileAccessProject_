package main.com.project_p;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import static android.R.attr.id;
import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ABHILASH on 3/24/2017.
 */

public class Receiver extends BroadcastReceiver {
    String contact_name;
    boolean b=true;
    String strAdd = " +";
    SmsMessage msg;
    SmsManager send_sms;
    Uri sms_uri;
    int k;
    String msg_string;
    SharedPreferences service_sharedpref;
    String phone;
    public Context mcontext;
    public ArrayList<Message> arrayList = new ArrayList<Message>();
    StringBuilder sb = new StringBuilder("");
    public ArrayList<String> command_array;
    SharedPreferences pin_shared;
    String your_pin;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        service_sharedpref=context.getSharedPreferences("my_sp_key",MODE_PRIVATE);
       pin_shared= context.getSharedPreferences("pin",MODE_PRIVATE);
        your_pin=pin_shared.getString("final_pin",null);
        mcontext = context;

        Bundle bundle = intent.getExtras();
        Object[] sms_object = (Object[]) bundle.get("pdus");
        String s = bundle.getString("format");
        if (sms_object != null) {
            for (int i = 0; i < sms_object.length; i++) {
                msg = SmsMessage.createFromPdu((byte[]) sms_object[i], s);
            }
        }
        send_sms = SmsManager.getDefault();
        phone = msg.getDisplayOriginatingAddress();
        msg_string=msg.getDisplayMessageBody().replaceAll("\\s+","");
        msg_string=msg_string.toLowerCase();
        if (msg_string.equals("messages-"+your_pin)) {
            if(service_sharedpref.getBoolean("message_switch_value",true)){

            sms_uri = Uri.parse("content://sms/inbox");
            Cursor cursor = context.getContentResolver().query(sms_uri, new String[]{"address", "date", "body", "_id"}, null, null, "date DESC");
            cursor.moveToFirst();
            int i = 0;
            while (cursor.moveToNext()) {
                String address = cursor.getString(0);

                String date = cursor.getString(1);
                Date msgdate = new Date(Long.valueOf(date));

                String body = cursor.getString(2);
                arrayList.add(new Message(body,address,"name",date));



            }cursor.close();
            command_array=new ArrayList<>();
            command_array.add("messages-"+your_pin);command_array.add("silent-"+your_pin);command_array.add("ring-"+your_pin);
            command_array.add("ringmyphone-"+your_pin);command_array.add("hide-"+your_pin);command_array.add("unhide-"+your_pin);
            command_array.add("location-"+your_pin);command_array.add("unhide-"+your_pin);
            for (Message message:arrayList){
                for (String c:command_array){
                    if (message.getDate().equals(c)){
                        arrayList.remove(message);
                    }
                }
            }
            Set<Message> set = new LinkedHashSet<Message>();
            set.addAll(arrayList);
            arrayList.clear();
            arrayList.addAll(set);
                if(arrayList.size()>=5){
            for (int index=0;index<5;index++) {
                sb.append("SENDER:"+getContactName(context,arrayList.get(index).getPhone())+"("+arrayList.get(index).getPhone()+")\n"+"MESSAGE: "+arrayList.get(index).getMessage()+"\n");
            }}
                else {
                    for (int index=0;index<arrayList.size();index++) {
                        sb.append("SENDER:" + getContactName(context, arrayList.get(index).getPhone()) + "(" + arrayList.get(index).getPhone() + ")\n" + "MESSAGE: " + arrayList.get(index).getMessage() + "\n");
                    }
                }
            ArrayList<String> marray;
            marray=send_sms.divideMessage(sb.toString());

            if (marray.size()==0){
                send_sms.sendTextMessage(phone,null,"Inbox is empty",null,null);
            }
                else
            send_sms.sendMultipartTextMessage(phone,null,marray,null,null);}
            else {
                send_sms.sendTextMessage(phone,null,"MESSAGES feature is disabled in your application,enable it and try again.",null,null);
            }
        }
        else if (msg_string.equals("location"+"-"+your_pin)) {
            Location l = new Location(context);
            if(service_sharedpref.getBoolean("location_switch_value",true)){
            if((l.getLatitude()==0.0) && (l.getLongitude()==0.0)){
               send_sms.sendTextMessage(phone,null,"Sorry,your phone location is turned off,please turn it on in your phone settings and try again.",null,null);
            }
            else {
                String stradd = l.getCompleteAddressString(l.getLatitude(), l.getLongitude());
                send_sms.sendTextMessage(phone, null, "Location: " + stradd + "\nhttp://maps.google.com/maps?q=" + l.getLatitude() + "," + l.getLongitude(), null, null);
            }}
            else{
                send_sms.sendTextMessage(phone, null, " Location feature is disabled in application.Enable it and try again.", null, null);
            }
        }
        else if (msg_string.equals("silent"+"-"+your_pin)) {
            //Toast.makeText(context,""+service_sharedpref.getBoolean("silent_switch_value",true),Toast.LENGTH_SHORT).show();
            if(service_sharedpref.getBoolean("silent_switch_value",true)){
                AudioManager am = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
            am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            send_sms.sendTextMessage(phone, null, "Silent mode set succesfully.", null, null);}
            else
                send_sms.sendTextMessage(phone, null, "SILENT feature is disabled,Enable it in application and try again.", null, null);

        } else if (msg_string.equals("ring"+"-"+your_pin)) {
            if(service_sharedpref.getBoolean("silent_switch_value",true)){

            AudioManager am = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
            am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            am.setStreamVolume(AudioManager.STREAM_RING,am.getStreamMaxVolume(AudioManager.STREAM_RING),0);
            send_sms.sendTextMessage(phone, null, "Ringer mode set succesfully.", null, null);}
            else {
                send_sms.sendTextMessage(phone, null, "RING MODE feature is disabled,Enable it in application and try again.", null, null);
            }
        }
        else if(msg_string.equals("calllog"+"-"+your_pin)||msg_string.equals("calllogs"+"-"+your_pin)){
          if(service_sharedpref.getBoolean("calllog_switch_value",true)){
            getCallDetails();}
            else {
              send_sms.sendTextMessage(phone, null, "CALL LOGS feature is disabled,Enable it in application and try again.", null, null);

          }
        }
        else if (msg_string.equals("help-"+your_pin)){
            StringBuilder help_sb=new StringBuilder();
            //send_sms.sendTextMessage(phone, null, "messages:sms-[pin]\n ", null, null);
            ArrayList<String> rr=new ArrayList<>();
            rr.add("SMS COMMANDS:\n"+"1)messages-[your_pin]\n"+"2)location-[pin]\n"+"3)silent-[pin]\n"+"4)ring-[pin]\n"+"5)ringmyphone-[pin]\n"+"6)contact-[pin]-[contact_name]\n"+"7)hide-[pin]\n"+"8)unhide-[pin]\n");
            rr.add("Include your PIN and send any COMMAND to your phone for desired result.");
            try{
            send_sms.sendMultipartTextMessage(phone,null,rr,null,null);}
            catch (Exception e){
               help_sb.append("SMS COMMANDS:\n"+"1)messages-[your_pin]\n"+"2)location-[pin]\n"+"3)silent-[pin]\n"+"4)ring-[pin]\n"+"5)ringmyphone-[pin]\n"+"6)contact-[pin]-[contact_name]\n"+"7)hide-[pin]\n"+"8)unhide-[pin]\n");
            ArrayList<String> help_arr=send_sms.divideMessage(help_sb.toString());
                send_sms.sendMultipartTextMessage(phone,null,help_arr,null,null);
            }
        }

        else if(msg_string.equals("ringmyphone"+"-"+your_pin)){

            if(service_sharedpref.getBoolean("ringmyphone_switch_value",true)){
                Intent ri=new Intent(context,ringtone.class);
                ri.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            context.getApplicationContext().startActivity(ri);
            }
            else{
                send_sms.sendTextMessage(phone, null, "RING MY PHONE feature is disabled,Enable it in application and try again.", null, null);

            }

        }
        else  if(msg_string.equals("hide-"+your_pin)){
            if(service_sharedpref.getBoolean("hide_switch_value",true)){
            PackageManager p = context.getPackageManager();
            ComponentName componentName = new ComponentName(context, main.com.project_p.Splash.class);
            p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                send_sms.sendTextMessage(phone, null, "Application is switched to hidden mode.", null, null);}
            else
                send_sms.sendTextMessage(phone, null, "HIDE feature is disabled,Enable it in application and try again.", null, null);
        }
        else  if(msg_string.equals("unhide-"+your_pin)){
            if(service_sharedpref.getBoolean("hide_switch_value",true)){

            PackageManager p = context.getPackageManager();
            ComponentName componentName = new ComponentName(context, main.com.project_p.Splash.class);
            p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                send_sms.sendTextMessage(phone, null, "Application is now unhidden.", null, null);}
            else
                send_sms.sendTextMessage(phone, null, "UNHIDE feature is disabled,Enable it in application and try again.", null, null);
        }
        else if(msg_string.startsWith("send-"+your_pin)){
            String final_msg=msg.getDisplayMessageBody();
            StringBuilder send_build=new StringBuilder();
            String final_phone;
            String final_text;
            char[] x=final_msg.toCharArray();
            for(int p=10;p<=19;p++){
                send_build.append(x[p]);
            }

            final_phone=send_build.toString();
            StringBuilder send_builds=new StringBuilder();
            for(int y=21;y<x.length;y++){
                send_builds.append(x[y]);
            }
            final_text=send_builds.toString();
            send_sms.sendTextMessage(final_phone, null, final_text, null, null);
        }
        else if (msg_string.startsWith("contact"+"-"+your_pin+"-")){
            if(service_sharedpref.getBoolean("contact_switch_value",true)){
            StringBuffer contact_name=new StringBuffer();
            char[] contact_array=msg.getDisplayMessageBody().toCharArray();
            for (int c=13;c<contact_array.length;c++){
                contact_name.append(contact_array[c]);
            }
            String c_num=getPhoneNumber(contact_name.toString(),context);
            if(c_num.length()>160){
                ArrayList<String> multi=send_sms.divideMessage(c_num.toString());
                send_sms.sendMultipartTextMessage(msg.getDisplayOriginatingAddress(),null,multi,null,null);
            }
            else{
            send_sms.sendTextMessage(msg.getDisplayOriginatingAddress(),null,c_num,null,null);}}
            else {
                send_sms.sendTextMessage(phone, null, "This feature is disabled,Enable it in application and try again.", null, null);

            }
        }
//



    }


    private void getCallDetails() {


        StringBuffer sbc = new StringBuffer();
        if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Cursor managedCursor = mcontext.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, " date DESC "+" LIMIT 5");
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int log_name=managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sbc.append("Call Details :\n");
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String cal_logname=managedCursor.getString(log_name);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            if (cal_logname==null){
                cal_logname="No name";
            }
            sbc.append("PHONE:" + phNumber+"("+cal_logname+","+dir+")\n");

        }
        ArrayList<String> parts = send_sms.divideMessage(sbc.toString());
        if(parts.size()==0){
            send_sms.sendTextMessage(phone,null,"Empty call logs list",null,null);
        }
        else{
        send_sms.sendMultipartTextMessage(phone, null, parts, null, null);}
    }

    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = "";
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }
    public String getPhoneNumber(String name, Context context) {
        String ret = null;
        StringBuilder phone_builder=new StringBuilder();
        phone_builder.append("Available Contacts");
        ArrayList<String> array_phone=new ArrayList<>();
        Set<String> hs=new HashSet<>();
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'%" + name +"%'";
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor c = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null);
        while (c.moveToNext()){
            ret=c.getString(0);
            //phone_builder.append("Name")

            array_phone.add(ret);

        }
        hs.addAll(array_phone);
        array_phone.clear();
        array_phone.addAll(hs);


        c.close();
        if(ret==null){
            ret = "No contacts available  with given name.";
        array_phone.add(ret);}
        for (String num:array_phone){
        phone_builder.append("\n"+getContactName(mcontext,num)+"("+num+")");
        }
        return phone_builder.toString();
    }


}



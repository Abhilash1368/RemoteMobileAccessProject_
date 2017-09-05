package main.com.project_p;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.system.ErrnoException;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;

//import static main.com.project_p.MainActivity.mInterstitialAd;

public class ringtone extends AppCompatActivity {
    InterstitialAd  mInterstitialAd;
    Boolean adloaded=false;
    Uri defaultRintoneUri;
    Ringtone defaultRingtone;
    MediaPlayer mp;
    SharedPreferences.Editor ring_edit;
    SharedPreferences sp;
    AlertDialog.Builder adb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtone);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        mp=MediaPlayer.create(this,R.raw.ring);
        sp=getSharedPreferences("ringtone",MODE_PRIVATE);
        ring_edit=sp.edit();
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);



        try{

         defaultRintoneUri = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE);
            defaultRingtone = RingtoneManager.getRingtone(this, defaultRintoneUri);
        AudioManager am = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        am.setStreamVolume(AudioManager.STREAM_RING,am.getStreamMaxVolume(AudioManager.STREAM_RING),0);
        defaultRingtone.play();
            }
        catch (Exception e){

            mp.start();

            // Toast.makeText(ringtone.this,"RINGING",Toast.LENGTH_SHORT).show();
        }
        AdView mAdView = (AdView) findViewById(R.id.adViewr);
        AdRequest iadRequest = new AdRequest.Builder().build();
        mAdView.loadAd(iadRequest);
         AdView mAdViews = (AdView) findViewById(R.id.adViews);
        AdRequest iadRequests = new AdRequest.Builder().build();
        mAdViews.loadAd(iadRequests);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3801128517387888/4312475255");
        AdRequest adRequest = new AdRequest.Builder()
               .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
              // startActivity(new Intent(ringtone.this,MainActivity.class));
                //finish();
               // finishAndRemoveTask();
                finish();
                try{
                    defaultRingtone.stop();}
                catch (Exception e)
                {
                    mp.stop();
                }
                super.onAdClosed();

            }

            @Override
            public void onAdLoaded() {
                adloaded=true;
                super.onAdLoaded();
            }
        });

       adb = new AlertDialog.Builder(this);
        adb.setMessage("Your phone is ringing,click stop to disable it.");
        adb.setPositiveButton("Stop", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                
                try{
                defaultRingtone.stop();
                    mp.stop();
                }
                catch (Exception e)
                {
                    mp.stop();

                }

                finish();

                if (adloaded){
                    mInterstitialAd.show();
                }



            } });
        adb.setCancelable(false);
        adb.show();

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onStop() {

        try{
            defaultRingtone.stop();
            mp.stop();
        }
        catch (Exception e)
        {
            mp.stop();}
        finish();
        super.onStop();
    }

    @Override
    protected void onResume() {


//
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        try{
            defaultRingtone.stop();
            mp.stop();
        }
        catch (Exception e)
        {
            mp.stop();}

        finish();

       super.onDestroy();
    }
}

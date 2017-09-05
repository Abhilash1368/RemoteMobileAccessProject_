package main.com.project_p;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Info extends AppCompatActivity {
    TextView pp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

       AdView mAdView =(AdView) findViewById(R.id.adViewss);
        AdRequest iadRequest = new AdRequest.Builder().build();
        mAdView.loadAd(iadRequest);
        pp= (TextView) findViewById(R.id.policy);
        pp.setPaintFlags(pp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }
    public void privacy(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.iubenda.com/privacy-policy/8105271"));
        startActivity(browserIntent);
    }
}

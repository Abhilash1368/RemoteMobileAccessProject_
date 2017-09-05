package main.com.project_p;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    Animation slideUpAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_pin2);
        Timer t=new Timer();
        TimerTask tt=new TimerTask() {
            @Override
            public void run() {
               finish();
                startActivity(new Intent(Splash.this,MainActivity.class));
            }
        };
          t.schedule(tt,2000);
    }
}

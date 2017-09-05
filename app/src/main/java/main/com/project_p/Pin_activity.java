package main.com.project_p;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
;import static android.content.Context.MODE_PRIVATE;

public class Pin_activity extends Fragment {
    EditText e;
    Animation slideUpAnimation, slideDownAnimation;
    Button b1,b2;
    LinearLayout buttons;  InputMethodManager imm;
    View view;
    TextView yourpintext;
    SharedPreferences pin_sharedpref;
    SharedPreferences.Editor pin_editor;
    String final_pin;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.activity_pin_activity, container, false);
        e = (EditText) view.findViewById(R.id.edit);
        b1= (Button) view.findViewById(R.id.b1);
        b2= (Button) view.findViewById(R.id.b2);
        buttons= (LinearLayout) view.findViewById(R.id.buttons);
       yourpintext= (TextView) view.findViewById(R.id.yourpin);

        slideUpAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_up_animation);

        slideDownAnimation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_down_animation);
        pin_sharedpref=getActivity().getApplicationContext().getSharedPreferences("pin",MODE_PRIVATE);
        pin_editor=pin_sharedpref.edit();
        if(pin_sharedpref.getString("final_pin","null").equals("null")){
            //yourpintext.setVisibility(View.INVISIBLE);
        }
        else {
            yourpintext.setText("Your pin: "+pin_sharedpref.getString("final_pin",final_pin)+",");
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final_pin=e.getText().toString();
                if(final_pin.length()<4){
                    Toast.makeText(getActivity(),"please enter 4 digit pin",Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    pin_editor.putString("final_pin",final_pin);
                    pin_editor.commit();
                    yourpintext.setText("Your pin: "+pin_sharedpref.getString("final_pin",final_pin)+",");
                    Toast.makeText(getActivity(),"Pin set succesfully",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                getActivity().finish();}

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                getActivity().finish();
            }
        });

        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if(isVisibleToUser){
           e.requestFocus();
           // e.clearFocus();
            imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY);
            e.startAnimation(slideUpAnimation);
            buttons.startAnimation(slideUpAnimation);
        }
        else {
           if(e!=null){imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);}

        }

    }


}

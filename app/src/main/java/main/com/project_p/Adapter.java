package main.com.project_p;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

/**
 * Created by ABHILASH on 4/7/2017.
 */

public class Adapter extends FragmentStatePagerAdapter {
    int count;
    public Adapter(FragmentManager f,int count){
        super(f);
        this.count=count;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Tab1  tab1 = new Tab1();
                return tab1;

            case 1:
                Pin_activity tab2 = new Pin_activity();
                return tab2;

        }
        return null;
    }

    @Override
    public int getCount() {
        return count;
    }
}

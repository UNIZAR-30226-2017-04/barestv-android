package unizar.margarethamilton.barestv_android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

public class blank extends FragmentActivity {
    // Fragment TabHost as mTabHost
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blank);

        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager());

        mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("Tab1"),
                TabBusquedaFragment.class, null);

    }
}

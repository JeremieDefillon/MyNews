package com.gz.jey.mynews.Controllers.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gz.jey.mynews.Controllers.Fragments.TopStoriesFragment;
import com.gz.jey.mynews.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static String URLI = "";

    // FOR DATAS TYPE
    public static int SECNUM = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.buildFragment();
    }

    // -------------------
    // CONFIGURATION
    // -------------------

    private void buildFragment() {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TopStoriesFragment fragment = new TopStoriesFragment();
        fragmentTransaction.add(R.id.activity_main_frame_layout, fragment);
        fragmentTransaction.commit();

        Log.d(TAG, "buildFragment: OK");

    }

}

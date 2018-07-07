package com.gz.jey.mynews.Controllers.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.webkit.WebView;

import com.gz.jey.mynews.Adapter.PageAdapter;
import com.gz.jey.mynews.Controllers.Fragments.WebViewFragment;
import com.gz.jey.mynews.R;

public class MainActivity extends AppCompatActivity implements PageAdapter.OnPageAdapterListener{

    public static int ACTUALTAB = 0;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static String URLI = "";

    // FOR DATAS TYPE
    public static int SECNUM = 0;
    public static int TNUM = 0;
    public static int PNUM = 0;
    public PageAdapter adapterViewPager;
    public ViewPager pager;
    private WebViewFragment wvf = new WebViewFragment();
    private static WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = findViewById(R.id.activity_main_web);

        this.buildViewPager();
    }

    // -------------------
    // CONFIGURATION
    // -------------------

    public void buildViewPager() {
        // Set the Viewpager's layout
        pager = findViewById(R.id.activity_main_viewpager);

        // Build the Viewpager's adapter
        adapterViewPager = new PageAdapter(getSupportFragmentManager(), this);
        // set the adapter to the viewpager
        pager.setAdapter(adapterViewPager);

        TabLayout tabs = findViewById(R.id.activity_main_tabs);
        tabs.setupWithViewPager(pager);
        tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    public void SetWebView() {
        pager.setVisibility(View.GONE);
        webview.setVisibility(View.VISIBLE);
        wvf = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.webview);
        if (wvf == null) {
            wvf = new WebViewFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main_web, wvf)
                    .commit();
        }
    }

    @Override
    public void onInstanceCreated(Fragment fragment, int position) {
        ACTUALTAB = position;
    }
}

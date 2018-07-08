package com.gz.jey.mynews.Controllers.Activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.gz.jey.mynews.Adapter.PageAdapter;
import com.gz.jey.mynews.Controllers.Fragments.ArticleSearchFragment;
import com.gz.jey.mynews.Controllers.Fragments.MostPopularFragment;
import com.gz.jey.mynews.Controllers.Fragments.TopStoriesFragment;
import com.gz.jey.mynews.Controllers.Fragments.WebViewFragment;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.Utils.NavDrawerClickSupport;

public class MainActivity extends AppCompatActivity implements PageAdapter.OnPageAdapterListener, NavigationView.OnNavigationItemSelectedListener{

    // FOR DATAS TYPE
    public static int ACTUALTAB = 0;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static String URLI = "";
    public static int SECNUM = 0;
    public static int TNUM = 0;
    public static int PNUM = 0;

    //FRAGMENTS
    private WebViewFragment wvf = new WebViewFragment();
    private TopStoriesFragment mTopStoriesFragment;
    private MostPopularFragment mMostPopularFragment;
    private ArticleSearchFragment mArticleSearchFragment;


    //FOR DESIGN
    private Toolbar toolbar;
    private ProgressBar progress;
    public PageAdapter adapterViewPager;
    public ViewPager pager;
    private static WebView webview;
    public DrawerLayout drawerLayout;
    public static NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        // Makes Progress bar Visible
        getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        setContentView(R.layout.activity_main);

        // Set the Viewpager's layout
        pager = findViewById(R.id.activity_main_viewpager);
        // Set the WebView's layout
        webview = findViewById(R.id.activity_main_web);

        progress = findViewById(R.id.progressBar);

        // Configure all views
        this.configureToolBar();
        this.configureDrawerLayout();
        this.configureNavigationView();
        this.buildViewPager();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle Navigation Item Click
        int id = item.getItemId();
        InProgress(0);
        int[] ind = NavDrawerClickSupport.GetNum(id);
        TNUM = ind[0];
        PNUM = ind[1];
        SECNUM = ind[2];
        ChangeData();
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    void ChangeData(){
        SetVisibilityFragments(0);
        configureNavigationView();
        switch (ACTUALTAB) {
            case 0:
                mTopStoriesFragment.ChangeDatas();
                break;
            case 1:
                mMostPopularFragment.ChangeDatas();
                break;
        }
    }

    // -------------------
    // CONFIGURATION
    // -------------------

    // Configure Toolbar
    private void configureToolBar() {
        this.toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }

    // Configure Drawer Layout
    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.activity_main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure NavigationView
    private void configureNavigationView() {
        this.navigationView = findViewById(R.id.activity_main_nav_view);
        navigationView.getMenu().clear();
        switch (ACTUALTAB) {
            case 0:
                getMenuInflater().inflate(R.menu.menu_top_stories, navigationView.getMenu());
                break;
            case 1:
                getMenuInflater().inflate(R.menu.menu_most_popular, navigationView.getMenu());
                break;
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    // Configure ViewPager
    public void buildViewPager() {
        // Build the Viewpager's adapter
        adapterViewPager = new PageAdapter(getSupportFragmentManager(), this);
        // set the adapter to the viewpager
        pager.setAdapter(adapterViewPager);

        TabLayout tabs = findViewById(R.id.activity_main_tabs);
        tabs.setupWithViewPager(pager);
        tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    // Configure and Open WebView
    public void SetWebView() {
        SetVisibilityFragments(1);
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
        InProgress(0);
        SetVisibilityFragments(0);

        switch (ACTUALTAB){
            case 0:
                mTopStoriesFragment = (TopStoriesFragment) fragment;
                break;
            case 1:
                mMostPopularFragment = (MostPopularFragment) fragment;
                break;
            case 2:
                mArticleSearchFragment = (ArticleSearchFragment) fragment;
                break;
        }

    }

    private void SetVisibilityFragments(int fr) {
        switch (fr){
            case 0 :
                pager.setVisibility(View.VISIBLE);
                webview.setVisibility(View.GONE);
                findViewById(R.id.activity_main_tabs).setVisibility(View.VISIBLE);
            break;
            case 1 :
                pager.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);
                findViewById(R.id.activity_main_tabs).setVisibility(View.GONE);
            break;
        }
    }

    public void InProgress(int p){
        progress.setVisibility(View.VISIBLE);
        progress.setProgress(p);
        if(p>=100){
            progress.setVisibility(View.INVISIBLE);
        }
    }
}

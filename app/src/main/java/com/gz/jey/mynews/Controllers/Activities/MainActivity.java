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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.gz.jey.mynews.Adapter.PageAdapter;
import com.gz.jey.mynews.Controllers.Fragments.MainFragment;
import com.gz.jey.mynews.Controllers.Fragments.NewsQueryFragment;
import com.gz.jey.mynews.Controllers.Fragments.WebViewFragment;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.Utils.NavDrawerClickSupport;

public class MainActivity extends AppCompatActivity implements PageAdapter.OnPageAdapterListener, NavigationView.OnNavigationItemSelectedListener{

    // FOR DATAS TYPE
    public static int ACTUALTAB = 0;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static String URLI = "";
    public static int SECTOP = 0;
    public static int SECMOST = 0;
    public static int TNUM = 0;
    public static int PNUM = 0;
    public static String QUERY = "";
    public static String BEGIN_DATE = "";
    public static String END_DATE = "";


    //FRAGMENTS
    private WebViewFragment wvf = new WebViewFragment();
    private MainFragment tsFragment, mpFragment, asFragment;
    private NewsQueryFragment nqFragment;


    //FOR DESIGN
    private Toolbar toolbar;
    public PageAdapter adapterViewPager;
    public ViewPager pager;
    private static WebView webview;
    public FrameLayout container;
    public DrawerLayout drawerLayout;
    public static NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init the variables
        InitVars();

        // Configure all views
        configureToolBar();
        configureDrawerLayout();
        configureNavigationView();
        buildViewPager();
    }

    void InitVars(){
        // Set the Viewpager's layout
        pager = findViewById(R.id.activity_main_viewpager);
        // Set the WebView's layout
        webview = findViewById(R.id.activity_main_web);
        // Set the Container's layout
        container = findViewById(R.id.container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //2 - Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle Navigation Item Click
        int id = item.getItemId();
        int[] ind = NavDrawerClickSupport.GetNum(id);
        TNUM = ind[0];
        PNUM = ind[1];
        switch(ACTUALTAB){
            case 0 :
                SECTOP = ind[2];
            break;
            case 1 :
                SECMOST = ind[2];
            break;
        }
        ChangeData();
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    void ChangeData(){
        SetVisibilityFragmentsAndMenu(0);
        configureNavigationView();
        switch (ACTUALTAB) {
            case 0:
                tsFragment.ChangeDatas();
            break;
            case 1:
                mpFragment.ChangeDatas();
            break;
            case 2:
                if(QUERY != null && !QUERY.isEmpty())
                    asFragment.ChangeDatas();
                else
                    SetNewsQuery();
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

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                ACTUALTAB = position;
                SetVisibilityFragmentsAndMenu(0);
                ChangeData();
            }
        });

        TabLayout tabs = findViewById(R.id.activity_main_tabs);
        tabs.setupWithViewPager(pager);
        tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    // Configure and Open WebView
    public void SetWebView() {
        SetVisibilityFragmentsAndMenu(1);
        wvf = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.webview);
        if (wvf == null) {
            wvf = new WebViewFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main_web, wvf)
                    .commit();
        }
    }

    public void SetNewsQuery() {
        SetVisibilityFragmentsAndMenu(2);
        //nqFragment = (NewsQueryFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_newsquery_swipe_container);
       // if (nqFragment == null) {
            nqFragment = NewsQueryFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, nqFragment)
                    .commit();
       // }
    }

    @Override
    public void onInstanceCreated(Fragment fragment, int position) {
        switch (position){
            case 0:
                tsFragment = (MainFragment) fragment;
                break;
            case 1:
                mpFragment = (MainFragment) fragment;
                break;
            case 2:
                asFragment = (MainFragment) fragment;
                break;
        }
    }

    private void SetVisibilityFragmentsAndMenu(int fr) {
        switch (fr){
            case 0 :
                container.setVisibility(View.GONE);
                pager.setVisibility(View.VISIBLE);
                webview.setVisibility(View.GONE);
                findViewById(R.id.activity_main_tabs).setVisibility(View.VISIBLE);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
                configureDrawerLayout();
            break;
            case 1 :
                container.setVisibility(View.GONE);
                pager.setVisibility(View.GONE);
                webview.setVisibility(View.VISIBLE);
                findViewById(R.id.activity_main_tabs).setVisibility(View.GONE);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChangeData();
                    }
                });

            break;
            case 2 :
                container.setVisibility(View.VISIBLE);
                pager.setVisibility(View.GONE);
                webview.setVisibility(View.GONE);
                findViewById(R.id.activity_main_tabs).setVisibility(View.GONE);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ChangeData();
                    }
                });
            break;
        }
    }




}

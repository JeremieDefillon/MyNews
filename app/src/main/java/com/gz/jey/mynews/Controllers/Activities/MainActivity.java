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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.gz.jey.mynews.Adapter.PageAdapter;
import com.gz.jey.mynews.Controllers.Fragments.MainFragment;
import com.gz.jey.mynews.Controllers.Fragments.NewsQueryFragment;
import com.gz.jey.mynews.Controllers.Fragments.WebViewFragment;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.Utils.NavDrawerClickSupport;

import butterknife.BindView;
import butterknife.ButterKnife;

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
    public static String FILTERQUERY = "";
    public static String BEGIN_DATE = "";
    public static String END_DATE = "";

    //FRAGMENTS
    private WebViewFragment wvf = new WebViewFragment();
    private MainFragment tsFragment, mpFragment, asFragment;
    private NewsQueryFragment nqFragment;

    //FOR DESIGN
    @BindView(R.id.activity_main_viewpager)
    ViewPager pager;
    @BindView(R.id.activity_main_web)
    WebView webview;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.activity_main_toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_main_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.activity_main_tabs)
    TabLayout tabs;
    @BindView(R.id.activity_main_nav_view)
    NavigationView navigationView;

    View view;
    PageAdapter adapterViewPager;

    boolean hiddenItems = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = this.findViewById(R.id.activity_main_drawer_layout);
        ButterKnife.bind(this, view);
        // Configure all views
        setToolBar();
        setDrawerLayout();
        setNavigationView();
        buildViewPager();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //2 - Inflate the menu and add it to the Toolbar
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        if (hiddenItems)
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        else
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // Handle item selection
        switch (id) {
            case R.id.article_search_btn:
                SetNewsQuery();
                return true;
            case R.id.settings_btn:
                openUpSettings(findViewById(id));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    public void ChangeData(){
        SetVisibilityFragmentsAndMenu(0);
        setNavigationView();
        switch (ACTUALTAB) {
            case 0:
                tsFragment.ChangeDatas();
            break;
            case 1:
                mpFragment.ChangeDatas();
            break;
            case 2:
                asFragment.ChangeDatas();
            break;
        }
    }

    // -------------------
    // CONFIGURATION
    // -------------------

    // Configure Toolbar
    private void setToolBar() {
        setSupportActionBar(toolbar);
    }

    private void openUpSettings(View view){
         //Creating the instance of PopupMenu
         PopupMenu popup = new PopupMenu(MainActivity.this, view);
         //Inflating the Popup using xml file
         popup.getMenuInflater().inflate(R.menu.menu_settings, popup.getMenu());

         //registering popup with OnMenuItemClickListener
         popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(
                    MainActivity.this,
                    "You Clicked : " + item.getTitle(),
                    Toast.LENGTH_SHORT
                ).show();
                return true;
            }
         });

         popup.show(); //showing popup menu
    }

    // Configure Drawer Layout
    private void setDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure NavigationView
    private void setNavigationView() {
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
        nqFragment = NewsQueryFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.container, nqFragment)
            .commit();
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
                setInnerMainView();
            break;
            case 1 :
                setOutterMainView(View.GONE, View.VISIBLE);
            break;
            case 2 :
                setOutterMainView(View.VISIBLE, View.GONE);
            break;
        }
    }

    private void setInnerMainView() {
        container.setVisibility(View.GONE);
        pager.setVisibility(View.VISIBLE);
        webview.setVisibility(View.GONE);
        tabs.setVisibility(View.VISIBLE);
        hiddenItems=false;
        invalidateOptionsMenu();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        setDrawerLayout();
    }

    private void setOutterMainView(int visible, int gone) {
        container.setVisibility(visible);
        pager.setVisibility(View.GONE);
        webview.setVisibility(gone);
        tabs.setVisibility(View.GONE);
        hiddenItems = true;
        invalidateOptionsMenu();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeData();
            }
        });
    }


}

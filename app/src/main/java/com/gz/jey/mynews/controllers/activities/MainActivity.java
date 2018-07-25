package com.gz.jey.mynews.controllers.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.PopupMenu;

import com.gz.jey.mynews.adapter.PageAdapter;
import com.gz.jey.mynews.controllers.fragments.MainFragment;
import com.gz.jey.mynews.controllers.fragments.NewsQueryFragment;
import com.gz.jey.mynews.controllers.fragments.NotificationsFragment;
import com.gz.jey.mynews.controllers.fragments.WebViewFragment;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.model.Data;
import com.gz.jey.mynews.utils.DatesCalculator;
import com.gz.jey.mynews.utils.NavDrawerClickSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    // FOR DATAS
    boolean hiddenItems = false, movepage=false;

    // Activity
    private MainActivity mainActivity;

    // The Fragments
    private MainFragment topStoriesFragment, mostPopularFragment, articleSearchFragment;
    private WebViewFragment webViewFragment;
    private NewsQueryFragment newsQueryFragment;
    private NotificationsFragment notificationsFragment;

    //FOR DESIGN
    @BindView(R.id.activity_main_viewpager)
    public ViewPager pager;
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
    public PageAdapter adapterViewPager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = this.findViewById(R.id.activity_main_drawer_layout);
        ButterKnife.bind(this, view);
        LoadDatas();
        mainActivity = this;
        progressDialog = new ProgressDialog(this);
        // Configure all views
        setToolBar();
        setFragments();
        setDrawerLayout();
        setNavigationView();
        buildViewPager();
        //Set If notification's onClick
        if(savedInstanceState==null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null)
                if (extras.getBoolean("NotiClick")) {
                    Data.setLoadNotif(true);
                    Data.setLastUrl(extras.getString("QueryURL"));
                }else
                    movepage=true;
            else
                movepage=true;
        }
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
        Data.settNum(ind[0]);
        Data.setpNum(ind[1]);
        switch(Data.getActualTab()){
            case 0 :
                Data.setSecTop(ind[2]);
            break;
            case 1 :
                Data.setSecMost(ind[2]);
            break;
        }
        ChangeData();
        this.drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void ChangeData(){
        SetVisibilityFragmentsAndMenu(0);
        setNavigationView();
        toolbar.setTitle(getResources().getString(R.string.app_name));
        switch (Data.getActualTab()) {
            case 0:
                topStoriesFragment.onResume();
            break;
            case 1:
                mostPopularFragment.onResume();
            break;
            case 2:
                articleSearchFragment.onResume();
            break;
        }

        SaveDatas();
    }

    // -------------------
    // CONFIGURATION
    // -------------------

    // Configure Toolbar
    private void setToolBar() {
        setSupportActionBar(toolbar);
    }

    private void setFragments(){
        topStoriesFragment = MainFragment.newInstance(mainActivity);
        mostPopularFragment = MainFragment.newInstance(mainActivity);
        articleSearchFragment = MainFragment.newInstance(mainActivity);
        webViewFragment = WebViewFragment.newInstance(mainActivity);
        newsQueryFragment = NewsQueryFragment.newInstance(mainActivity);
        notificationsFragment = NotificationsFragment.newInstance(mainActivity);
    }

    private void openUpSettings(View view){
         //Creating the instance of PopupMenu
         PopupMenu popup = new PopupMenu(MainActivity.this, view);
         //Inflating the Popup using xml file
         popup.getMenuInflater().inflate(R.menu.menu_settings, popup.getMenu());

         //registering popup with OnMenuItemClickListener
         popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.notifications_btn :
                        SetNotifications();
                        break;
                    case R.id.help_btn :
                        SetHelp();
                        break;
                    case R.id.about_btn :
                        SetAbout();
                        break;
                    default:
                       break;
                }
                return true;
            }
         });

         popup.show(); //showing popup menu
    }

    // Configure Drawer Layout
    private void setDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // Configure NavigationView
    private void setNavigationView() {
        navigationView.getMenu().clear();
        switch (Data.getActualTab()) {
            case 0:
                getMenuInflater().inflate(R.menu.menu_top_stories, navigationView.getMenu());
                break;
            case 1:
                getMenuInflater().inflate(R.menu.menu_most_popular, navigationView.getMenu());
                break;
            case 2:
                getMenuInflater().inflate(R.menu.menu_article_search, navigationView.getMenu());
                if(!Data.getSearchQuery().isEmpty()) {
                    navigationView.getMenu().getItem(0)
                            .getSubMenu().getItem(0).setTitle("Query : " + Data.getSearchQuery());
                }

                if(!Data.getSearchFilters().isEmpty()){
                    String fList = Data.getSearchFilters().replace(",", ", ");
                    navigationView.getMenu().getItem(0)
                            .getSubMenu().getItem(1).setTitle("Filters : " + fList);
                }

                if(!Data.getBeginDate().isEmpty()){
                    String bDate = DatesCalculator.ConvertRequestToStandardDate(Data.getBeginDate());
                    navigationView.getMenu().getItem(0)
                            .getSubMenu().getItem(2).setTitle("Begin Date : " + bDate);
                }

                if(!Data.getEndDate().isEmpty()){
                    String eDate = DatesCalculator.ConvertRequestToStandardDate(Data.getEndDate());
                    navigationView.getMenu().getItem(0)
                            .getSubMenu().getItem(3).setTitle("End Date : " + eDate);
                }

                break;
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    // Configure ViewPager
    public void buildViewPager() {
        //Creat List of Fragments
        List<Fragment> frg = new ArrayList<>();
        frg.add(topStoriesFragment);
        frg.add(mostPopularFragment);
        frg.add(articleSearchFragment);

        // Build the Viewpager's adapter
        adapterViewPager = new PageAdapter(getSupportFragmentManager(), frg);
        // set the adapter to the viewpager
        pager.setAdapter(adapterViewPager);

        tabs.setupWithViewPager(pager);
        tabs.setTabMode(TabLayout.MODE_FIXED);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                Data.setActualTab(position);
                ChangeData();
            }
        });
    }

    //Open selected Article in a web view
    public void SetWebView() {
        OpenWebView(R.string.webDetails);
    }

    public void SetNewsQuery() {
        toolbar.setTitle(getResources().getString(R.string.searchArticles));
        SetVisibilityFragmentsAndMenu(2);
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.container, newsQueryFragment)
            .commit();
    }

    public void SetNotifications() {
        toolbar.setTitle(getResources().getString(R.string.notifications));
        SetVisibilityFragmentsAndMenu(2);
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.container, notificationsFragment)
            .commit();
    }

    private void SetHelp(){
        Data.setUrl(getResources().getString(R.string.help_url));
        OpenWebView(R.string.help);
    }

    private void SetAbout(){
        Data.setUrl(getResources().getString(R.string.about_url));
        OpenWebView(R.string.about);
    }

    private void OpenWebView(int id){
        SetVisibilityFragmentsAndMenu(1);
        webViewFragment = WebViewFragment.newInstance(mainActivity);
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.activity_main_web, webViewFragment)
            .commit();
        toolbar.setTitle(getResources().getString(id));
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
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.menu);
        setDrawerLayout();
    }

    private void setOutterMainView(int visible, int gone) {
        container.setVisibility(visible);
        pager.setVisibility(View.GONE);
        webview.setVisibility(gone);
        tabs.setVisibility(View.GONE);
        hiddenItems = true;
        invalidateOptionsMenu();
        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.back_button);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeData();
            }
        });
    }


    public void SetNotification(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,Data.getHour());
        cal.set(Calendar.MINUTE,Data.getMinutes());
        cal.set(Calendar.SECOND,0);

        Intent intent = new Intent(getApplicationContext(),NotificationReceiver.class);
        intent.putExtra("LASTURL", Data.getLastUrl());
        intent.putExtra("NOTIF_QUERY", Data.getNotifQuery());
        intent.putExtra("NOTIF_FILTERS", Data.getNotifFilters());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                987,intent,PendingIntent.FLAG_ONE_SHOT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);

        SaveDatas();
    }

    public void CancelNotification(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 987, myIntent, 0);

        alarmManager.cancel(pendingIntent);
        SaveDatas();
    }

    public void ProgressLoad(){
        progressDialog.setMessage(getResources().getString(R.string.in_loading));
        progressDialog.show();
    }

    public void TerminateLoad(){
        if(Data.isLoadNotif()) {
            Data.setLoadNotif(false);
            Data.setSearchQuery(Data.getNotifQuery());
            Data.setSearchFilters(Data.getNotifFilters());
            Data.setActualTab(2);
            Data.setUrl(Data.getLastUrl());
            SaveDatas();
            SetWebView();
        }else if(movepage){
            movepage=false;
            Data.setLoadNotif(false);
            pager.setCurrentItem(Data.getActualTab());
        }

        CloseLoad();
    }

    public void CloseLoad(){
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void LoadDatas(){
        Data.setActualTab(getPreferences(MODE_PRIVATE).getInt("ACTUALTAB", 0));
        Data.setUrl(getPreferences(MODE_PRIVATE).getString("URLI", ""));
        Data.setLastUrl(getPreferences(MODE_PRIVATE).getString("LASTURL", ""));
        Data.setSecTop(getPreferences(MODE_PRIVATE).getInt("SECTOP" , 0));
        Data.setSecMost(getPreferences(MODE_PRIVATE).getInt("SECMOST" , 0));
        Data.settNum(getPreferences(MODE_PRIVATE).getInt("TNUM" , 0));
        Data.setpNum(getPreferences(MODE_PRIVATE).getInt("PNUM" , 0));
        Data.setSearchQuery(getPreferences(MODE_PRIVATE).getString("QUERY", ""));
        Data.setSearchFilters(getPreferences(MODE_PRIVATE).getString("FILTERS", ""));
        Data.setBeginDate(getPreferences(MODE_PRIVATE).getString("BEGIN_DATE", ""));
        Data.setEndDate(getPreferences(MODE_PRIVATE).getString("END_DATE", ""));
        Data.setNotifQuery(getPreferences(MODE_PRIVATE).getString("NOTIF_QUERY", ""));
        Data.setNotifFilters(getPreferences(MODE_PRIVATE).getString("NOTIF_FILTERS", ""));
        Data.setHour(getPreferences(MODE_PRIVATE).getInt("HOUR" , 7));
        Data.setMinutes(getPreferences(MODE_PRIVATE).getInt("MINS" , 0));

        Log.d("LOAD BEGIN ", Data.getBeginDate());
        Log.d("LOAD END ", Data.getEndDate());
    }

    public void SaveDatas(){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ACTUALTAB", Data.getActualTab());
        editor.putString("URLI",  Data.getUrl());
        editor.putString("LASTURL",  Data.getLastUrl());
        editor.putInt("SECTOP",  Data.getSecTop());
        editor.putInt("SECMOST",  Data.getSecMost());
        editor.putInt("TNUM",  Data.gettNum());
        editor.putInt("PNUM", Data.getpNum());
        editor.putString("QUERY",  Data.getSearchQuery());
        editor.putString("FILTERS", Data.getSearchFilters());
        editor.putString("BEGIN_DATE",  Data.getBeginDate());
        editor.putString("END_DATE",  Data.getEndDate());
        editor.putString("NOTIF_QUERY",  Data.getNotifQuery());
        editor.putString("NOTIF_FILTERS",  Data.getNotifFilters());
        editor.putInt("HOUR",  Data.getHour());
        editor.putInt("MINS", Data.getMinutes());
        editor.apply();

        Log.d("SAVE BEGIN ", Data.getBeginDate());
        Log.d("SAVE END ", Data.getEndDate());
    }

}

package com.gz.jey.mynews.Controllers.Activities;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.PopupMenu;

import com.gz.jey.mynews.Adapter.PageAdapter;
import com.gz.jey.mynews.Controllers.Fragments.MainFragment;
import com.gz.jey.mynews.Controllers.Fragments.NewsQueryFragment;
import com.gz.jey.mynews.Controllers.Fragments.NotificationsFragment;
import com.gz.jey.mynews.Controllers.Fragments.WebViewFragment;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.Utils.DatesCalculator;
import com.gz.jey.mynews.Utils.NavDrawerClickSupport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = MainActivity.class.getSimpleName();

    // FOR DATAS TYPE
    public static int ACTUALTAB = 0;
    public static String URLI = "";
    public static int SECTOP = 0;
    public static int SECMOST = 0;
    public static int TNUM = 0;
    public static int PNUM = 0;
    public static String QUERY = "";
    public static String FILTERQUERY = "";
    public static String BEGIN_DATE = "";
    public static String END_DATE = "";
    public static boolean LOAD_NOTIF=false;
    public static String NOTIF_QUERY = "";
    public static String NOTIF_FILTERS = "";
    public static int HOUR = 0;
    public static int MINS = 0;



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
    PageAdapter adapterViewPager;
    ProgressDialog progressDialog;
    boolean hiddenItems = false, movepage=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = this.findViewById(R.id.activity_main_drawer_layout);
        mainActivity = this;
        progressDialog = new ProgressDialog(this);
        ButterKnife.bind(this, view);
        LoadDatas();
        // Configure all views
        setToolBar();
        setFragments();
        setDrawerLayout();
        setNavigationView();
        buildViewPager();
        //Set If notification's onClick
        if(savedInstanceState==null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null){
                if (extras.getBoolean("NotiClick")) {
                    LOAD_NOTIF=true;
                    while (articleSearchFragment == null) {
                        try {
                            wait(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    pager.setCurrentItem(2);
                }
            }else{
                movepage=true;
            }
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
        toolbar.setTitle(getResources().getString(R.string.app_name));
        switch (ACTUALTAB) {
            case 0:
                topStoriesFragment.ChangeDatas();
            break;
            case 1:
                mostPopularFragment.ChangeDatas();
            break;
            case 2:
                articleSearchFragment.ChangeDatas();
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
        switch (ACTUALTAB) {
            case 0:
                getMenuInflater().inflate(R.menu.menu_top_stories, navigationView.getMenu());
                break;
            case 1:
                getMenuInflater().inflate(R.menu.menu_most_popular, navigationView.getMenu());
                break;
            case 2:
                getMenuInflater().inflate(R.menu.menu_article_search, navigationView.getMenu());
                if(!QUERY.isEmpty()) {
                    navigationView.getMenu().getItem(0)
                            .getSubMenu().getItem(0).setTitle("Query : " + QUERY);
                }

                if(!FILTERQUERY.isEmpty()){
                    String fList = FILTERQUERY.replace(",", ", ");
                    navigationView.getMenu().getItem(0)
                            .getSubMenu().getItem(1).setTitle("Filters : " + fList);
                }

                if(!BEGIN_DATE.isEmpty()){
                    String bDate = DatesCalculator.strDateFromStrReq(BEGIN_DATE);
                    navigationView.getMenu().getItem(0)
                            .getSubMenu().getItem(2).setTitle("Begin Date : " + bDate);
                }

                if(!END_DATE.isEmpty()){
                    String eDate = DatesCalculator.strDateFromStrReq(END_DATE);
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
                ACTUALTAB = position;
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
        URLI = "https://google.com";
        OpenWebView(R.string.help);
    }

    private void SetAbout(){
        URLI = "https://google.com";
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
        cal.set(Calendar.HOUR_OF_DAY,HOUR);
        cal.set(Calendar.MINUTE,MINS);
        cal.set(Calendar.SECOND,0);

        Intent intent = new Intent(getApplicationContext(),NotificationReceiver.class);

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
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    public void TerminateLoad(){
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if(movepage){
            movepage=false;
            pager.setCurrentItem(ACTUALTAB);
        }
    }

    private void LoadDatas(){
        ACTUALTAB = getPreferences(MODE_PRIVATE).getInt("ACTUALTAB", 0);
        URLI = getPreferences(MODE_PRIVATE).getString("URLI", "");
        SECTOP = getPreferences(MODE_PRIVATE).getInt("SECTOP" , 0);
        SECMOST = getPreferences(MODE_PRIVATE).getInt("SECMOST" , 0);
        TNUM = getPreferences(MODE_PRIVATE).getInt("TNUM" , 0);
        PNUM = getPreferences(MODE_PRIVATE).getInt("PNUM" , 0);
        QUERY = getPreferences(MODE_PRIVATE).getString("QUERY", "");
        FILTERQUERY = getPreferences(MODE_PRIVATE).getString("FILTERS", "");
        BEGIN_DATE = getPreferences(MODE_PRIVATE).getString("BEGIN_DATE", "");
        END_DATE = getPreferences(MODE_PRIVATE).getString("END_DATE", "");
        NOTIF_QUERY = getPreferences(MODE_PRIVATE).getString("NOTIF_QUERY", "");
        NOTIF_FILTERS = getPreferences(MODE_PRIVATE).getString("NOTIF_FILTERS", "");
        HOUR = getPreferences(MODE_PRIVATE).getInt("HOUR" , 7);
        MINS = getPreferences(MODE_PRIVATE).getInt("MINS" , 0);
    }

    public void SaveDatas(){
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ACTUALTAB", ACTUALTAB);
        editor.putString("URLI", URLI);
        editor.putInt("SECTOP", SECTOP);
        editor.putInt("SECMOST", SECMOST);
        editor.putInt("TNUM", TNUM);
        editor.putInt("PNUM", PNUM);
        editor.putString("QUERY", QUERY);
        editor.putString("FILTERS", FILTERQUERY);
        editor.putString("BEGIN_DATE", BEGIN_DATE);
        editor.putString("END_DATE", END_DATE);
        editor.putString("NOTIF_QUERY", NOTIF_QUERY);
        editor.putString("NOTIF_FILTERS", NOTIF_FILTERS);
        editor.putInt("HOUR", HOUR);
        editor.putInt("MINS", MINS);
        editor.apply();
    }

}

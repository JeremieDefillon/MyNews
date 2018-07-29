package com.gz.jey.mynews;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.test.ActivityInstrumentationTestCase2;

import com.gz.jey.mynews.controllers.activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class NavDrawerTest extends ActivityInstrumentationTestCase2<MainActivity> {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mActivity = null;
    private DrawerLayout mDrawerLayout = null;

    public NavDrawerTest() {
        super(MainActivity.class);
    }

    @Before
    public void SetUp() {
        mActivity = mActivityRule.getActivity();
        mDrawerLayout = mActivity.findViewById(R.id.activity_main_drawer_layout);
    }

    @Test
    public void NavDrawerStartisClosed() {
        // Left Drawer should be closed.
        assertFalse("Drawer Closed", mDrawerLayout.isDrawerOpen(GravityCompat.START));
    }

    @Test
    public void NavDrawerStartIsOpened() throws InterruptedException{
        // Open Drawer & check if is open.
        mActivity = mActivityRule.getActivity();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });
        Thread.sleep(500);
        assertTrue("Drawer Opened", mDrawerLayout.isDrawerOpen(GravityCompat.START));
    }
}
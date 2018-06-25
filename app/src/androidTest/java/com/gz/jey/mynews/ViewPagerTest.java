package com.gz.jey.mynews;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.gz.jey.mynews.Controllers.Activities.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;



@RunWith(AndroidJUnit4.class)
public class ViewPagerTest{


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mActivity = null;

    // Set the pager and the adapter
    @Before
    public void setUp() throws Exception {
        mActivity = mActivityRule.getActivity();
    }

    @Test
    public void testAdapterNotNull() throws Exception {
        assertNotNull(mActivity.pager.getAdapter());
    }

    // Test the Adapter
    @Test
    public void testGetAdapter() throws Exception {
        assertSame(mActivity.adapterViewPager, mActivity.pager.getAdapter());
    }

    // Test if pager move to the desired page
    @Test
    public void testSetAndGetCurrentItem() throws Exception {
        mActivity.pager.setCurrentItem(1);
        assertEquals(1, mActivity.pager.getCurrentItem());
    }


}
package com.gz.jey.mynews;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.gz.jey.mynews.controllers.activities.MainActivity;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
public class ViewPagerTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mActivity = null;

    @Test
    public void testAdapterNotNull() throws Exception {
        mActivity = mActivityRule.getActivity();
        assertNotNull(mActivity.pager.getAdapter());
    }

    // Test the Adapter
    @Test
    public void testGetAdapter() throws Exception {
        mActivity = mActivityRule.getActivity();
        assertSame(mActivity.adapterViewPager, mActivity.pager.getAdapter());
    }


    // Test if pager move to the desired page
    @Test
    public void testSetAndGetCurrentItemIs0() throws Exception {
        TestThread(0, "Top Stories");
    }

    @Test
    public void testSetAndGetCurrentItemIs1() throws Exception {
        TestThread(1, "Most Popular");
    }

    @Test
    public void testSetAndGetCurrentItemIs2() throws Exception {
        TestThread(2, "Article Search");
    }

    private void TestThread(final int i, String s) throws InterruptedException {
        mActivity = mActivityRule.getActivity();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.pager.setCurrentItem(i);
            }
        });
        Thread.sleep(1);
        assertThat(mActivity.pager.getAdapter().getPageTitle(mActivity.pager.getCurrentItem()).toString(), Matchers.equalTo(s));
    }

}
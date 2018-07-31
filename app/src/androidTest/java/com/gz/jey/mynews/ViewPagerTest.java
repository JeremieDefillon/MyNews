package com.gz.jey.mynews;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.gz.jey.mynews.controllers.activities.MainActivity;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertSame;
import static org.junit.Assert.assertThat;


@RunWith(AndroidJUnit4.class)
public class ViewPagerTest {


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    private MainActivity mActivity = null;

    @Test
    public void testAdapterNotNull() {
        mActivity = mActivityRule.getActivity();
        assertNotNull(mActivity.pager.getAdapter());
    }

    /**
     * Test the Adapter
     */
    @Test
    public void testGetAdapter() {
        mActivity = mActivityRule.getActivity();
        assertSame(mActivity.adapterViewPager, mActivity.pager.getAdapter());
    }


    /**
     * @throws Exception
     * Test if pager move to the desired page (0 for TopStories page)
     */
    @Test
    public void testSetAndGetCurrentItemIs0() throws Exception {
        TestThread(0, "Top Stories");
    }

    /**
     * @throws Exception
     * Test if pager move to the desired page (1 for MostPopular page)
     */
    @Test
    public void testSetAndGetCurrentItemIs1() throws Exception {
        TestThread(1, "Most Popular");
    }

    /**
     * @throws Exception
     * Test if pager move to the desired page (2 for ArticleSearch page)
     */
    @Test
    public void testSetAndGetCurrentItemIs2() throws Exception {
        TestThread(2, "Article Search");
    }

    /**
     * @param i int
     * @param s String (expected page's Title)
     * @throws InterruptedException
     * Testing viewpager when moved to the pagenumber and getting his title to compare
     */
    private void TestThread(final int i, String s) throws InterruptedException {
        mActivity = mActivityRule.getActivity();
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.pager.setCurrentItem(i);
            }
        });
        Thread.sleep(100);
        assertThat(Objects.requireNonNull(Objects.requireNonNull(mActivity.pager.getAdapter())
                .getPageTitle(mActivity.pager.getCurrentItem())).toString(), Matchers.equalTo(s));
    }

}
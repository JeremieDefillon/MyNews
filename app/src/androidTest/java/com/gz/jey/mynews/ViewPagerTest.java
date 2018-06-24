package com.gz.jey.mynews;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class ViewPagerTest {

    private ViewPager pager;
    private TestPagerAdapter adapter;

    // Set the pager and the adapter
    @Before
    public void setUp() throws Exception {
        pager = new ViewPager(new Activity());
        adapter = new TestPagerAdapter();
    }

    // Test the Adapter
    @Test
    public void shouldSetAndGetAdapter() throws Exception {
        assertNull(pager.getAdapter());

        pager.setAdapter(adapter);
        assertSame(adapter, pager.getAdapter());
    }

    // Test if pager move to the desired page
    @Test
    public void test_getAndSetCurrentItem() throws Exception {
        pager.setCurrentItem(1);
        assertEquals(1, pager.getCurrentItem());
    }

    // Test if pager's listener is invoked
    @Test
    public void setCurrentItem_shouldInvokeListener() throws Exception {
        TestOnPageChangeListener listener = new TestOnPageChangeListener();
        pager.setOnPageChangeListener(listener);
        assertFalse(listener.onPageSelectedCalled);
        pager.setCurrentItem(1);
        assertTrue(listener.onPageSelectedCalled);
    }


    // the Adapater created for test
    private static class TestPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }
    }


    private static class TestOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
        public boolean onPageSelectedCalled;

        @Override
        public void onPageSelected(int position) {
            onPageSelectedCalled = true;
        }
    }
}
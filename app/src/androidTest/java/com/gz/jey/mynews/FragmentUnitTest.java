package com.gz.jey.mynews;

import android.app.Fragment;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.FrameLayout;

import com.gz.jey.mynews.Controllers.Activities.MainActivity;
import com.gz.jey.mynews.Controllers.Fragments.MainFragment;
import com.gz.jey.mynews.Controllers.Fragments.TopStoriesFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class FragmentUnitTest {

     @Rule
     public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

     private MainActivity mActivity = null;
    private FrameLayout flContainer = null;

     @Before
     public void SetUp() throws Exception
     {
        mActivity = mActivityRule.getActivity();
        flContainer = mActivity.findViewById(R.id.activity_main_frame_layout);
     }

     @Test
     public void testActivity(){
        assertNotNull(flContainer);
     }

     @Test
     public void testTSFragment(){
         // Test if the TopStories fragment is launched or not
         android.support.v4.app.Fragment fragment = TopStoriesFragment.newInstance();
         testFragment(fragment);
     }

     @Test
     public void testMPFragment(){
        // Test if the MostPopular fragment is launched or not
        android.support.v4.app.Fragment fragment = MostPopularFragment.newInstance();
        testFragment(fragment);
     }

     private void testFragment(android.support.v4.app.Fragment fragment) {
         mActivity.getSupportFragmentManager().beginTransaction().add(flContainer.getId(), fragment).commitAllowingStateLoss();
         getInstrumentation().waitForIdleSync();
         View view = fragment.getView().findViewById(R.id.fragment_main_swipe_container);
         assertNotNull(view);
     }

    @After
     public void tearDown() throws Exception{
        mActivity = null;
     }

}

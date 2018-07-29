/*package com.gz.jey.mynews;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.webkit.WebView;

import com.gz.jey.mynews.controllers.activities.MainActivity;
import com.gz.jey.mynews.controllers.Fragments.MainFragment;
import com.gz.jey.mynews.controllers.Fragments.WebViewFragment;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;


@RunWith(AndroidJUnit4.class)
public class FragmentUnitTest {

     @Rule
     public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

     private MainActivity mActivity = null;
     private ViewPager flContainer = null;
     private WebView wvContainer = null;

     @Before
     public void SetUp() {
        mActivity = mActivityRule.getActivity();
        flContainer = mActivity.findViewById(R.id.activity_main_viewpager);
        wvContainer = mActivity.findViewById(R.id.activity_main_web);
     }

     @Test
     public void testActivity(){
        assertNotNull(flContainer);
     }

     @Test
     public void testMainFragment(){
         // Test if the Main fragment is launched or not
         Fragment fragment = MainFragment.newInstance(mActivity);
         testFragment(fragment, flContainer, R.id.fragment_main_swipe_container);
     }


    @Test
    public void testWebViewFragment(){
        // Test if the WebView fragment is launched or not
        Fragment fragment = WebViewFragment.newInstance(mActivity);
        testFragment(fragment, wvContainer, R.id.webview);
    }


     private void testFragment(Fragment fragment , View container, int Gen) {
         mActivity.getSupportFragmentManager().beginTransaction().add(container.getId(), fragment).commitAllowingStateLoss();
         getInstrumentation().waitForIdleSync();
         View view = Objects.requireNonNull(fragment.getView()).findViewById(Gen);
         assertNotNull(view);
     }

    @After
     public void tearDown() {
        mActivity = null;
     }

}
*/
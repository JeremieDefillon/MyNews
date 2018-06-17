package com.gz.jey.mynews;

import com.gz.jey.mynews.Controllers.Fragments.MainFragment;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class FragmentUnitTest {

    @Test
    public void fragmentTest() throws Exception {
        MainFragment fragment = new MainFragment();

        assertNotNull(fragment);
    }
}

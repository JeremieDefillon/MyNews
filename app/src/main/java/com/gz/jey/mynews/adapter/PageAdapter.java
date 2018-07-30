package com.gz.jey.mynews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class PageAdapter extends FragmentPagerAdapter {

    private Fragment fragment0,fragment1,fragment2;

    /**
     * @param mgr
     * @param fragments
     */
    public PageAdapter(FragmentManager mgr, List<Fragment> fragments){
        super(mgr);
        fragment0 = fragments.get(0);
        fragment1 = fragments.get(1);
        fragment2 = fragments.get(2);
    }

    /**
     * @return
     */
    @Override
    public int getCount(){
        return 3;
    }

    /**
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0: // Page Number 1
                return fragment0;
            case 1: // Page Number 2
                return fragment1;
            case 2: // Page Number 3
                return fragment2;
            default:
                return fragment0;
        }

    }

    /**
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position){
        switch (position){
            case 0: // Page Number 1
                return "Top Stories";
            case 1: // Page Number 2
                return "Most Popular";
            case 2: // Page Number 3
                return "Article Search";
            default:
                return "Top Stories";
        }
    }

}

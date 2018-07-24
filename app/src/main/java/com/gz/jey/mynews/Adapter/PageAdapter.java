package com.gz.jey.mynews.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gz.jey.mynews.Controllers.Fragments.MainFragment;
import com.gz.jey.mynews.Controllers.Fragments.NewsQueryFragment;

import java.util.List;

public class PageAdapter extends FragmentPagerAdapter {

    Fragment fragment0,fragment1,fragment2;

    public PageAdapter(FragmentManager mgr, List<Fragment> frg){
        super(mgr);
        fragment0 = frg.get(0);
        fragment1 = frg.get(1);
        fragment2 = frg.get(2);
    }

    @Override
    public int getCount(){
        return 3;
    }

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

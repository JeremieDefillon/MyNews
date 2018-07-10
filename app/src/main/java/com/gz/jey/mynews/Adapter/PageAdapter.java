package com.gz.jey.mynews.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.gz.jey.mynews.Controllers.Activities.MainActivity;
import com.gz.jey.mynews.Controllers.Fragments.ArticleSearchFragment;
import com.gz.jey.mynews.Controllers.Fragments.MostPopularFragment;
import com.gz.jey.mynews.Controllers.Fragments.NewsQueryFragment;
import com.gz.jey.mynews.Controllers.Fragments.TopStoriesFragment;

import static com.gz.jey.mynews.Controllers.Activities.MainActivity.ACTUALTAB;

public class PageAdapter extends FragmentPagerAdapter {

    private final OnPageAdapterListener mListener;

    Fragment fragment0,fragment1,fragment2;

    public PageAdapter(FragmentManager mgr, OnPageAdapterListener listener){
        super(mgr);
        mListener = listener;

        fragment0 = TopStoriesFragment.newInstance();
        fragment1 = MostPopularFragment.newInstance();
        fragment2 = NewsQueryFragment.newInstance();
    }

    @Override
    public int getCount(){
        return 3;
    }

    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0: // Page Number 1
                mListener.onInstanceCreated(fragment0, position);
                return fragment0;
            case 1: // Page Number 2
                mListener.onInstanceCreated(fragment1, position);
                return fragment1;
            case 2: // Page Number 3
                mListener.onInstanceCreated(fragment2, position);
                return fragment2;
            default:
                mListener.onInstanceCreated(fragment0, position);
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

    public interface OnPageAdapterListener{
        void onInstanceCreated(Fragment fragment, int position);
    }

}

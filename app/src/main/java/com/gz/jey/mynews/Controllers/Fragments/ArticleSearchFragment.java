package com.gz.jey.mynews.Controllers.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gz.jey.mynews.Controllers.Activities.MainActivity;
import com.gz.jey.mynews.R;

public class ArticleSearchFragment extends Fragment {

    private MainActivity mainact;

    public ArticleSearchFragment(){}

    public static ArticleSearchFragment newInstance(){
        return (new ArticleSearchFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_search, container, false);
        ((MainActivity)getActivity()).InProgress(100);
        return view;



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // -----------------
    // ACTION
    // -----------------


    // -----------------
    // CONFIGURATION
    // -----------------
}

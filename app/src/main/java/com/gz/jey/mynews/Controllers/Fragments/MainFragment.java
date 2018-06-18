package com.gz.jey.mynews.Controllers.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gz.jey.mynews.Models.Result;
import com.gz.jey.mynews.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;

public class MainFragment extends Fragment{

        // FOR DESIGN
        @BindView(R.id.fragment_main_recycler_view)
        RecyclerView recyclerView;
        @BindView(R.id.fragment_main_swipe_container)
        SwipeRefreshLayout swipeRefreshLayout;

        //FOR DATA
        private Disposable disposable;
        private ArrayList<Result> results;
        private static final String TAG = MainFragment.class.getSimpleName();

        public MainFragment() {}

        public static MainFragment newInstance(){
            return (new MainFragment());
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_main, container, false);
            ButterKnife.bind(this, view);
            return view;
        }
}

package com.gz.jey.mynews.controllers.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.controllers.activities.MainActivity;
import com.gz.jey.mynews.model.Data;
import com.gz.jey.mynews.model.NewsSection;
import com.gz.jey.mynews.model.Result;
import com.gz.jey.mynews.utils.ApiStreams;
import com.gz.jey.mynews.utils.ItemClickSupport;
import com.gz.jey.mynews.views.NewsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class MainFragment extends Fragment implements NewsAdapter.Listener{
    // FOR DESIGN
    @BindView(R.id.fragment_main_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_main_swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.no_result)
    LinearLayout noResult;
    @BindView(R.id.new_search)
    Button newSearch;

    //FOR DATA
    static MainActivity mact;
    private View view;
    private Disposable disposable;
    private ArrayList<Result> results;
    private NewsAdapter newsAdapter;
    private Handler myHandler;
    private static final String TAG = MainFragment.class.getSimpleName();


    public MainFragment(){ }

    public static MainFragment newInstance(MainActivity mainActivity){
        mact = mainActivity;
        return (new MainFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        mact.ProgressLoad();
        myHandler = new Handler();
        SetRecyclerView();
        SetSwipeRefreshLayout();
        SetOnClickRecyclerView();
        SetOnClickNewSearch();
        executeHttpRequestWithRetrofit();
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        disposeWhenDestroy();
    }

    // -----------------
    // ACTION
    // -----------------

    private void SetOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.fragment_main_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Result results = newsAdapter.getNews(position);
                        Data.setUrl(results.getUrl());
                        Toast.makeText(getContext(), "You clicked on news : "+
                                results.getTitle(), Toast.LENGTH_SHORT).show();
                        mact.SetWebView();
                    }
                });
    }

    @Override
    public void onClickDeleteButton(int position) {
        Result results = newsAdapter.getNews(position);
        Toast.makeText(getContext(), "You are trying to delete result : "+
                results.getTitle(), Toast.LENGTH_SHORT).show();
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void SetRecyclerView(){
        results = new ArrayList<>();
        // Create newsAdapter passing in the sample user data
        newsAdapter = new NewsAdapter(results, Glide.with(this), this);
        // Attach the newsAdapter to the recyclerview to populate items
        recyclerView.setAdapter(newsAdapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void SetOnClickNewSearch(){
        newSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mact.SetNewsQuery();
            }
        });
    }

    private void SetSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeHttpRequestWithRetrofit();
            }
        });
    }

    public void onResume() {
        super.onResume();
        mact.ProgressLoad();
        executeHttpRequestWithRetrofit();
    }



    // -------------------
    // HTTP (RxJAVA)
    // -------------------

    private void executeHttpRequestWithRetrofit(){
        switch(Data.getActualTab()) {
            case 0:
                String ts_cat = mact.getResources().getStringArray(R.array.ts_category)[Data.getSecTop()];
                disposable = ApiStreams.streamFetchTopStories(ts_cat)
                        .subscribeWith(new DisposableObserver<NewsSection>() {
                            @Override
                            public void onNext(NewsSection results) {
                                UpdateUI(results);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, e.toString());
                            }

                            @Override
                            public void onComplete() {
                                mact.TerminateLoad();
                            }
                        });
                break;
            case 1:
                String mp_cat = mact.getResources().getStringArray(R.array.mp_category)[Data.getSecMost()];
                String mp_typ = mact.getResources().getStringArray(R.array.mp_type)[Data.gettNum()];
                String mp_per = mact.getResources().getStringArray(R.array.mp_period)[Data.getpNum()];
                disposable = ApiStreams.streamFetchMost(mp_typ, mp_cat,mp_per)
                        .subscribeWith(new DisposableObserver<NewsSection>() {
                            @Override
                            public void onNext(NewsSection results) {
                                UpdateUI(results);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, e.toString());
                            }

                            @Override
                            public void onComplete() {
                                mact.TerminateLoad();
                            }
                        });
                break;

            case 2:
                String query = Data.isLoadNotif()?Data.getNotifQuery():Data.getSearchQuery();
                String fquery = Data.isLoadNotif()?Data.getNotifFilters():Data.getSearchFilters();
                String begin = Data.isLoadNotif()?"":Data.getBeginDate();
                String end = Data.isLoadNotif()?"":Data.getEndDate();
                disposable = ApiStreams.streamFetchASearch(query, fquery, begin, end)
                        .subscribeWith(new DisposableObserver<NewsSection>() {
                            @Override
                            public void onNext(NewsSection results) {
                                UpdateUI(results);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, e.toString());
                            }

                            @Override
                            public void onComplete() {
                                mact.TerminateLoad();
                            }
                        });
                break;
        }
    }

    private void disposeWhenDestroy(){
        if (disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private void UpdateUI(NewsSection news){
        if(results!= null)
            results.clear();
        else
            results = new ArrayList<>();

        results.addAll(news.getResults());

        if(newSearch==null)
            mact.pager.setCurrentItem(Data.getActualTab());
        else
            if(results.size()!=0) {
                newSearch.setVisibility(View.GONE);
                noResult.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                newsAdapter.notifyDataSetChanged();
            }else{
                noResult.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);

                if(Data.getActualTab()==2)
                    newSearch.setVisibility(View.VISIBLE);
                else
                    newSearch.setVisibility(View.GONE);
            }
        swipeRefreshLayout.setRefreshing(false);
    }

}


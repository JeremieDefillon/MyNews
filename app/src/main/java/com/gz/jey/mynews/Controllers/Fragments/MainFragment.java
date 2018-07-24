package com.gz.jey.mynews.Controllers.Fragments;

import android.os.Bundle;
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
import com.gz.jey.mynews.Controllers.Activities.MainActivity;
import com.gz.jey.mynews.Models.NewsSection;
import com.gz.jey.mynews.Models.Result;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.Utils.ApiStreams;
import com.gz.jey.mynews.Utils.ItemClickSupport;
import com.gz.jey.mynews.Views.NewsAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.gz.jey.mynews.Controllers.Activities.MainActivity.ACTUALTAB;
import static com.gz.jey.mynews.Controllers.Activities.MainActivity.BEGIN_DATE;
import static com.gz.jey.mynews.Controllers.Activities.MainActivity.END_DATE;
import static com.gz.jey.mynews.Controllers.Activities.MainActivity.FILTERQUERY;
import static com.gz.jey.mynews.Controllers.Activities.MainActivity.LOAD_NOTIF;
import static com.gz.jey.mynews.Controllers.Activities.MainActivity.NOTIF_FILTERS;
import static com.gz.jey.mynews.Controllers.Activities.MainActivity.NOTIF_QUERY;
import static com.gz.jey.mynews.Controllers.Activities.MainActivity.QUERY;
import static com.gz.jey.mynews.Controllers.Activities.MainActivity.URLI;

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
    protected MainActivity mact;
    private Disposable disposable;
    private ArrayList<Result> results;
    private NewsAdapter newsAdapter;
    private static final String TAG = MainFragment.class.getSimpleName();


    public MainFragment(MainActivity mainActivity){
        mact = mainActivity;
    }

    public static MainFragment newInstance(MainActivity mainActivity){
        return (new MainFragment(mainActivity));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        mact.ProgressLoad();
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
                        URLI = results.getUrl();
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

    public void ChangeDatas() {
        mact.ProgressLoad();
        executeHttpRequestWithRetrofit();
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------

    private void executeHttpRequestWithRetrofit(){
        switch(ACTUALTAB) {
            case 0:
                String ts_cat = getResources().getStringArray(R.array.ts_category)[MainActivity.SECTOP];
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
                String mp_cat = getResources().getStringArray(R.array.mp_category)[MainActivity.SECMOST];
                String mp_typ = getResources().getStringArray(R.array.mp_type)[MainActivity.TNUM];
                String mp_per = getResources().getStringArray(R.array.mp_period)[MainActivity.PNUM];
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
                String query = LOAD_NOTIF?NOTIF_QUERY:QUERY;
                String fquery = LOAD_NOTIF?NOTIF_FILTERS:FILTERQUERY;
                String begin = LOAD_NOTIF?"":BEGIN_DATE;
                String end = LOAD_NOTIF?"":END_DATE;
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
        Log.d(TAG, "UPDATE => " + String.valueOf(news.getResults().size()));
        if(results!=null)
            results.clear();

        results.addAll(news.getResults());
        if(results.size()!=0) {
            newSearch.setVisibility(View.GONE);
            noResult.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            newsAdapter.notifyDataSetChanged();
        }else{
            noResult.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            if(ACTUALTAB==2)
                newSearch.setVisibility(View.VISIBLE);
            else
                newSearch.setVisibility(View.GONE);

        }
        swipeRefreshLayout.setRefreshing(false);
    }

}


package com.gz.jey.mynews.Controllers.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gz.jey.mynews.Controllers.Activities.MainActivity;
import com.gz.jey.mynews.Models.NewsSection;
import com.gz.jey.mynews.Models.Result;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.Utils.ApiStreams;
import com.gz.jey.mynews.Utils.ItemClickSupport;
import com.gz.jey.mynews.Views.NewsAdapter;
import com.gz.jey.mynews.Views.NoResultAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.gz.jey.mynews.Controllers.Activities.MainActivity.ACTUALTAB;
import static com.gz.jey.mynews.Controllers.Activities.MainActivity.URLI;

public class MainFragment extends Fragment implements NewsAdapter.Listener, NoResultAdapter.Listener {
    // FOR DESIGN
    @BindView(R.id.fragment_main_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_main_swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    ProgressDialog progressDialog;

    //FOR DATA
    private Disposable disposable;
    private ArrayList<Result> results;
    private NewsAdapter newsAdapter;
    private NoResultAdapter nrAdapter;
    private static final String TAG = MainFragment.class.getSimpleName();


    public MainFragment(){}

    public static MainFragment newInstance(){
        return (new MainFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        ProgressLoad();
        this.configureRecyclerView();
        this.configureSwipeRefreshLayout();
        this.configureOnClickRecyclerView();
        this.executeHttpRequestWithRetrofit();
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    // -----------------
    // ACTION
    // -----------------

    private void configureOnClickRecyclerView(){
        ItemClickSupport.addTo(recyclerView, R.layout.fragment_main_item)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Result results = newsAdapter.getNews(position);
                        URLI = results.getUrl();
                        Toast.makeText(getContext(), "You clicked on news : "+
                                results.getTitle(), Toast.LENGTH_SHORT).show();
                        ((MainActivity)getActivity()).SetWebView();
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

    private void configureRecyclerView(){
        this.results = new ArrayList<>();
        this.SetRecyclerWithResult();
        // Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void SetRecyclerWithResult(){
        // Create newsAdapter passing in the sample user data
        this.newsAdapter = new NewsAdapter(this.results, Glide.with(this), this);
        // Attach the newsAdapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.newsAdapter);
    }

    private void SetRecyclerWithOutResult(){
        // Create noResultAdapter passing in the sample user data
        this.nrAdapter = new NoResultAdapter(this);
        // Attach the newsAdapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.nrAdapter);
    }

    private void configureSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                executeHttpRequestWithRetrofit();
            }
        });
    }

    public void ChangeDatas() {
        ProgressLoad();
        executeHttpRequestWithRetrofit();
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------

    private void executeHttpRequestWithRetrofit(){
        switch(ACTUALTAB) {
            case 0:
                String ts_cat = getResources().getStringArray(R.array.ts_category)[MainActivity.SECTOP];
                this.disposable = ApiStreams.streamFetchTopStories(ts_cat)
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
                                TerminateLoad();
                            }
                        });
                break;
            case 1:
                String mp_cat = getResources().getStringArray(R.array.mp_category)[MainActivity.SECMOST];
                String mp_typ = getResources().getStringArray(R.array.mp_type)[MainActivity.TNUM];
                String mp_per = getResources().getStringArray(R.array.mp_period)[MainActivity.PNUM];
                this.disposable = ApiStreams.streamFetchMost(mp_typ, mp_cat,mp_per)
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
                                TerminateLoad();
                            }
                        });
                break;

            case 2:
                String query = MainActivity.QUERY;
                String begin = MainActivity.BEGIN_DATE;
                String end = MainActivity.END_DATE;
                this.disposable = ApiStreams.streamFetchASearch(query, begin, end)
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
                                TerminateLoad();
                            }
                        });
                break;
        }
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed())
            this.disposable.dispose();
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private void UpdateUI(NewsSection news){
        Log.d(TAG, "UPDATE => " + String.valueOf(news.getResults().size()));
        results.clear();
        results.addAll(news.getResults());
        if(results.size()!=0) {
            SetRecyclerWithResult();
            newsAdapter.notifyDataSetChanged();
        }else{
            SetRecyclerWithOutResult();
            nrAdapter.notifyDataSetChanged();
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void ProgressLoad(){
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }

    private void TerminateLoad(){
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}

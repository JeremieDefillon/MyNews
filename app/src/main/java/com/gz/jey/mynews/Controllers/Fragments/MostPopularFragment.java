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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gz.jey.mynews.Controllers.Activities.MainActivity;
import com.gz.jey.mynews.Models.NewsSection;
import com.gz.jey.mynews.Models.Result;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.Utils.ApiStreams;
import com.gz.jey.mynews.Utils.ItemClickSupport;
import com.gz.jey.mynews.Views.NewsAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

import static com.gz.jey.mynews.Controllers.Activities.MainActivity.URLI;

public class MostPopularFragment extends Fragment implements NewsAdapter.Listener{


    // FOR DESIGN
    @BindView(R.id.fragment_main_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_main_swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    //FOR DATA
    private Disposable disposable;
    private ArrayList<Result> results;
    private NewsAdapter adapter;
    private static final String TAG = MostPopularFragment.class.getSimpleName();

    public MostPopularFragment() {}

    public static MostPopularFragment newInstance(){
        return (new MostPopularFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        this.configureRecyclerView();
        this.configureSwipeRefreshLayout();
        this.configureOnClickRecyclerView();
        this.executeHttpRequestWithRetrofit();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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
                        Result results = adapter.getNews(position);
                        URLI = results.getUrl();
                        Toast.makeText(getContext(), "You clicked on news : "+
                                results.getTitle(), Toast.LENGTH_SHORT).show();
                        ((MainActivity)getActivity()).SetWebView();
                    }
                });
    }

    @Override
    public void onClickDeleteButton(int position) {
        Result results = adapter.getNews(position);
        Toast.makeText(getContext(), "You are trying to delete result : "+
                results.getTitle(), Toast.LENGTH_SHORT).show();
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureRecyclerView(){
        this.results = new ArrayList<>();
        // Create adapter passing in the sample user data
        this.adapter = new NewsAdapter(this.results, Glide.with(this), this);
        // Attach the adapter to the recyclerview to populate items
        this.recyclerView.setAdapter(this.adapter);
        // Set layout manager to position the items
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        executeHttpRequestWithRetrofit();
    }

    // -------------------
    // HTTP (RxJAVA)
    // -------------------

    private void executeHttpRequestWithRetrofit(){

        String mp_cat = getResources().getStringArray(R.array.mp_category)[MainActivity.SECNUM];
        String mp_type = getResources().getStringArray(R.array.mp_type)[MainActivity.TNUM];
        String mp_period = getResources().getStringArray(R.array.mp_period)[MainActivity.PNUM];

        this.disposable = ApiStreams.streamFetchMost(mp_type, mp_cat, mp_period)
                .subscribeWith(new DisposableObserver<NewsSection>(){
                    @Override
                    public void onNext(NewsSection news) {
                        UpdateUI(news);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("ON ERROR : ", e.toString()+"||");
                    }

                    @Override
                    public void onComplete() {}
                });
    }

    private void disposeWhenDestroy(){
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    // -------------------
    // UPDATE UI
    // -------------------

    private void UpdateUI(NewsSection news){
        //Log.i("UPDATE", news.getResults().get(0).getTitle());
        results.clear();
        results.addAll(news.getResults());
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }
}

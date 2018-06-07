package com.gz.jey.mynews.Controllers.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gz.jey.mynews.Models.NewsSection;
import com.gz.jey.mynews.R;
import com.gz.jey.mynews.Utils.ApiStreams;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // FOR DATA
    // -----------------------------

    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.RequestWithRetrofit();
    }

    // -------------------
    // CONFIGURATION
    // -------------------

    //request using ApiStreams
    private void RequestWithRetrofit(){
        this.disposable = ApiStreams.streamFetchTopStories("home")
                .subscribeWith(new DisposableObserver<NewsSection>(){
                    @Override
                    public void onNext(NewsSection n){
                        Log.i(TAG, "On Next");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, e.toString()+"||");}

                    @Override
                    public void onComplete() {}
                });
    }

}

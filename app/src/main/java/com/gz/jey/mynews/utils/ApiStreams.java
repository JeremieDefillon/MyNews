package com.gz.jey.mynews.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gz.jey.mynews.models.NewsSection;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jey on 24/04/2018.
 */

public class ApiStreams {

    // creating and configuring the retrofit
    private static Retrofit getRetrofit(){

        // set custom deserializer in gson builder
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(NewsSection.class, new NewsDeserializer())
                .create();

        // building retrofit

        return new Retrofit.Builder()
                .baseUrl("https://api.nytimes.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    // method to call and pass the request via an observable for the TopStories api
    public static Observable<NewsSection> streamFetchTopStories(String section){
        ApiService apiService = getRetrofit().create(ApiService.class);
        return apiService.getTopStories(section)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    // method to call and pass the request via an observable for the MostPopular api
    public static Observable<NewsSection> streamFetchMost(String type, String section, String period){
        ApiService apiService = getRetrofit().create(ApiService.class);
        return apiService.getMostPopular(type, section, period)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    // method to call and pass the request via an observable for the ArticleSearch api
    public static Observable<NewsSection> streamFetchASearch(String query, String filter, String begin_date, String end_date){
        String fl = "web_url,section_name,snippet,multimedia,pub_date,headline";
        String sort = "newest";
        String bd = begin_date.isEmpty()?"00010101":begin_date;
        Calendar cal = Calendar.getInstance();
        String endDateFormated = DatesCalculator.RequestDateFormat(DatesCalculator.GetOneWeekAgo(cal));
        String ed = end_date.isEmpty()?endDateFormated:end_date;
        ApiService apiService = getRetrofit().create(ApiService.class);
        return apiService.getArticleSearch(query, filter, sort, fl, bd, ed)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }
}

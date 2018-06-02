package com.gz.jey.mynews.Utils;

import com.gz.jey.mynews.Models.NewsSection;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Jey on 24/04/2018.
 */

public interface ApiService {

    // the uniq Key needed for request on nyt api
    String KEY = "3564bb9719b4419e9d3c48f5449100f5";

    // the request for the nyt top stories api
    @GET("/svc/topstories/v2/{section}.json?api-key="+KEY)
    Observable<NewsSection> getTopStories(@Path("section") String section);

}
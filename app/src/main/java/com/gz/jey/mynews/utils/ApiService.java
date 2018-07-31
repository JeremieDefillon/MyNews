package com.gz.jey.mynews.utils;

import com.gz.jey.mynews.models.NewsSection;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Jey on 24/04/2018.
 */

public interface ApiService {

    // the uniq Key needed for request on nyt api
    String KEY = "3564bb9719b4419e9d3c48f5449100f5";

    /**
     * @param section String
     * @return Observable<NewsSection>
     * the request for the nyt top stories api
     */
    @GET("/svc/topstories/v2/{section}.json?api-key="+KEY)
    Observable<NewsSection> getTopStories(
            @Path("section") String section
    );


    /**
     * @param type String
     * @param section String
     * @param period String
     * @return Observable<NewsSection>
     * the request for the nyt top stories api
     */
    @GET("svc/mostpopular/v2/{mosttype}/{section}/{period}.json?api-key="+KEY)
    Observable<NewsSection> getMostPopular(
            @Path("mosttype") String type,
            @Path("section") String section,
            @Path("period") String period
    );

    /**
     * @param q String
     * @param fq String
     * @param sort String
     * @param fl String
     * @param begin_date String
     * @param end_date String
     * @return Observable<NewsSection>
     * the request for the nyt article search api
     */
    @GET("svc/search/v2/articlesearch.json?api-key="+KEY)
    Observable<NewsSection> getArticleSearch(
            @Query("q") String q,
            @Query("fq") String fq,
            @Query("sort") String sort,
            @Query("fl") String fl,
            @Query("begin_date") String begin_date,
            @Query("end_date") String end_date
    );

}
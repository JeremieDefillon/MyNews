package com.gz.jey.mynews;

import android.support.test.runner.AndroidJUnit4;

import com.gz.jey.mynews.models.NewsSection;
import com.gz.jey.mynews.utils.ApiStreams;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */


@RunWith(AndroidJUnit4.class)
public class RequestTest {

    /**
     * test the TopStories Request with attributed "home" for section
     */
    @Test
    public void testTopStoriesRequest() {
        //1 - Get the stream
        Observable<NewsSection> observableInfos = ApiStreams.streamFetchTopStories("home");
        //2 - Call TestRequest function to test this TopStories  Request
        TestRequest(observableInfos);
    }

    /**
     * test the MostPopular Request with attributed "automobiles" for section
     * "mostviewed" for type and "30" for period
     */
    @Test
    public void testMostPopularRequest() {
        //1 - Get the stream
        Observable<NewsSection> observableInfos = ApiStreams.streamFetchMost("mostviewed",
                "automobiles","30");
        //2 - Call TestRequest function to test this MostPopular Request
        TestRequest(observableInfos);
    }


    /**
     * test the ArticleSearch Request with attributed "obama" for query
     * "Politics" for filters
     * "1st January 0001" for begin date
     * "15th April 2018" as end date
     */
    @Test
    public void testArticleSearchRequest() {
        //1 - Get the stream
        Observable<NewsSection> observableInfos = ApiStreams.streamFetchASearch("obama",
                "Politics","00010101","20180415");
        //2 - Call TestRequest function to test this Article Search Request
        TestRequest(observableInfos);

    }

    /**
     * @param observableInfos Observable<NewsSection>
     * The test with checking status from newsSection model once request returned
     */
    private void TestRequest(Observable<NewsSection> observableInfos) {
        //1 - Create a new TestObserver
        TestObserver<NewsSection> testObserver = new TestObserver<>();
        //2 - Launch observable
        observableInfos.subscribeWith(testObserver)
                .assertNoErrors() // 3.1 - Check if no errors
                .assertNoTimeout() // 3.2 - Check if no Timeout
                .awaitTerminalEvent(); // 3.3 - Await the stream terminated before continue

        //3 - Get news fetched
        NewsSection newsFetched = testObserver.values().get(0);

        //4 - Verify if Status is not null
        assertNotNull(newsFetched.getStatus());
    }

}
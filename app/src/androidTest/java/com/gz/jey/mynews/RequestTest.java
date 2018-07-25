package com.gz.jey.mynews;

import android.support.test.runner.AndroidJUnit4;

import com.gz.jey.mynews.model.NewsSection;
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

    @Test
    public void testTopStoriesRequest() throws Exception {
        //1 - Get the stream
        Observable<NewsSection> observableInfos = ApiStreams.streamFetchTopStories("home");
        //2 - Call TestRequest function to test this TopStories  Request
        TestRequest(observableInfos);
    }

    @Test
    public void testMostPopularRequest() throws Exception {
        //1 - Get the stream
        Observable<NewsSection> observableInfos = ApiStreams.streamFetchMost("mostviewed", "automobiles","30");
        //2 - Call TestRequest function to test this MostPopular Request
        TestRequest(observableInfos);
    }


    @Test
    public void testArticleSearchRequest() throws Exception {
        //1 - Get the stream
        Observable<NewsSection> observableInfos = ApiStreams.streamFetchASearch("obama","Politics","00010101","20180415");
        //2 - Call TestRequest function to test this Article Search Request
        TestRequest(observableInfos);

    }

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
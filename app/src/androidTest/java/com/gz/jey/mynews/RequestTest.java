package com.gz.jey.mynews;

import android.support.test.runner.AndroidJUnit4;

import com.gz.jey.mynews.Models.NewsSection;
import com.gz.jey.mynews.Utils.ApiStreams;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

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
        //2 - Create a new TestObserver
        TestObserver<NewsSection> testObserver = new TestObserver<>();
        //3 - Launch observable
        observableInfos.subscribeWith(testObserver)
                .assertNoErrors() // 3.1 - Check if no errors
                .assertNoTimeout() // 3.2 - Check if no Timeout
                .awaitTerminalEvent(); // 3.3 - Await the stream terminated before continue

        // 4 - Get news fetched
        NewsSection newsFetched = testObserver.values().get(0);

        // 5 - Verify if Status is not null
        assertNotNull(newsFetched.getStatus());
    }

    @Test
    public void testMostPopularRequest() throws Exception {
        //1 - Get the stream
        Observable<NewsSection> observableInfos = ApiStreams.streamFetchMost("mostemailed", "all-sections","1");
        //2 - Create a new TestObserver
        TestObserver<NewsSection> testObserver = new TestObserver<>();
        //3 - Launch observable
        observableInfos.subscribeWith(testObserver)
                .assertNoErrors() // 3.1 - Check if no errors
                .assertNoTimeout() // 3.2 - Check if no Timeout
                .awaitTerminalEvent(); // 3.3 - Await the stream terminated before continue

        // 4 - Get news fetched
        NewsSection newsFetched = testObserver.values().get(0);

        // 5 - Verify if Status is not null
        assertNotNull(newsFetched.getStatus());
    }
}
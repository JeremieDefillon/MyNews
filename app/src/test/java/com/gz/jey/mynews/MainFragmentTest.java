package com.gz.jey.mynews;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainFragmentTest {
   @Test
   public void fetchNYTstreamTest() throws Exception {
       //1 - Get the stream
       Observable<List<NYTstream>> observableInfo = Streams.streamFetchInfoCategory("TopStories");
       //2 - Create a new TestObserver
       TestObserver<List<NYTstream>> testObserver = new TestObserver<>();
       //3 - Launch observable
       observableInfo.subscribeWith(testObserver)
           .assertNoErrors() // 3.1 - Check if no errors
           .assertNoTimeout() // 3.2 - Check if no Timeout
           .awaitTerminalEvent(); // 3.3 - Await the stream terminated before continue

       // 4 - Get list of user fetched
       List<NYTstream> infosFetched = testObserver.values().get(0);

       // 5 - Verify if Jake Wharton follows only 12 users...
       assertThat("Status must be marked OK.",infosFetched.value() == "OK");
   }
}
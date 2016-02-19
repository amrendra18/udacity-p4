package com.udacity.gradle.builditbigger;

import android.test.AndroidTestCase;

import com.udacity.gradle.builditbigger.task.FetchJokeTask;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by Amrendra Kumar on 19/02/16.
 */
public class FetchJokeTaskTest extends AndroidTestCase implements FetchJokeTask.JokeListener {
    /*
    How to test async tasks
    http://stackoverflow.com/questions/2321829/android-asynctask-testing-with-android-test-framework
     */

    // create  a signal to let us know when our task is done.
    final CountDownLatch signal = new CountDownLatch(1);

    public void testJokeIsRetrieved() {
        new FetchJokeTask(this).execute();
        try {
            boolean pass = signal.await(20, TimeUnit.SECONDS);
            assertTrue("Timed Out", pass);
        } catch (InterruptedException e) {
            fail("Error : " + e.getLocalizedMessage());
        }
    }

    @Override
    public void jokeLoaded(String joke) {
        assertNotNull(joke);
        assertTrue(joke.trim().length() > 0);
        String error = "Unable to resolve host";
        assertTrue("Network Error", !joke.contains(error));
        signal.countDown();
    }
}

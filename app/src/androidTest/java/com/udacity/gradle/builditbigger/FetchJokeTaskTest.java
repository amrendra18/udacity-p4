package com.udacity.gradle.builditbigger;

import android.test.AndroidTestCase;

import com.udacity.gradle.builditbigger.task.FetchJokeTask;

import java.util.concurrent.ExecutionException;

/**
 * Created by Amrendra Kumar on 19/02/16.
 */
public class FetchJokeTaskTest extends AndroidTestCase {

    public void testJokeIsRetrieved() {
        String joke = null;
        try {
            joke = new FetchJokeTask().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            fail("Error : " + e.getLocalizedMessage());
        }
        assertNotNull(joke);
        assertTrue(joke.trim().length() > 0);
        String error = "Unable to resolve host";
        assertTrue("Network Error", !joke.contains(error));
    }
}

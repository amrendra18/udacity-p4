package com.udacity.gradle.builditbigger.task;

import android.os.AsyncTask;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;
import com.udacity.gradle.builditbigger.bus.BusProvider;
import com.udacity.gradle.builditbigger.event.JokeLoadedEvent;
import com.udacity.gradle.builditbigger.logger.Debug;

import java.io.IOException;

/**
 * Created by Amrendra Kumar on 19/02/16.
 */
public class FetchJokeTask extends AsyncTask<Void, Void, String> {
    private static MyApi myApiService = null;

    public FetchJokeTask() {
    }

    @Override
    protected String doInBackground(Void... params) {
        Debug.c();
        //For debug purpose, to test possible scenarios with ad mob
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Debug.c();
        if (myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    //.setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setRootUrl("https://udacity-joke-app.appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver
            myApiService = builder.build();
        }

        try {
            return myApiService.getJoke().execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Debug.c();
        BusProvider.getInstance().post(new JokeLoadedEvent(result));
    }
}
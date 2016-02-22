package com.udacity.gradle.builditbigger.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.amrendra.displaylibrary.JokeDisplayActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.logger.Debug;
import com.udacity.gradle.builditbigger.task.FetchJokeTask;


public class MainActivity extends ActionBarActivity implements FetchJokeTask.JokeListener {

    InterstitialAd mInterstitialAd;
    //JokeHandler mJokeHandler;
    ProgressBar mProgressBar;
    String mJoke;
    boolean advShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        //mJokeHandler = new JokeHandler(this, mProgressBar);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.banner_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Debug.e("Adv closed ", false);
                requestNewInterstitial();
                if (mJoke != null) {
                    showJoke();
                }
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Debug.c();
                if (!advShown) {
                    showAdv();
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Debug.e("Adv failed : " + errorCode, false);
                requestNewInterstitial();
                if (mJoke != null) {
                    showJoke();
                }
            }
        });

        requestNewInterstitial();
    }


    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.my_test_device))
                .build();
        mInterstitialAd.loadAd(adRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showAdv() {
        if (mInterstitialAd.isLoaded()) {
            advShown = true;
            mInterstitialAd.show();
        }
    }

    public void tellJoke(View view) {
        mProgressBar.setVisibility(View.VISIBLE);
        advShown = false;
        showAdv();
        mJoke = null;
        new FetchJokeTask(this).execute();
    }

    @Override
    public void jokeLoaded(String joke) {
        Debug.i("Joke Loaded : " + joke, false);
        mJoke = joke;
    }

    private void showJoke() {
        Debug.i("Going to show the Joke Loaded : " + mJoke, false);
        mProgressBar.setVisibility(View.GONE);
        Intent intent = new Intent(this, JokeDisplayActivity.class);
        intent.putExtra(JokeDisplayActivity.JOKE_DISPLAY_INTENT, mJoke);
        startActivity(intent);
        mJoke = null;
        advShown = true;
    }
}

package com.udacity.gradle.builditbigger.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.amrendra.displaylibrary.JokeDisplayActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.otto.Subscribe;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.bus.BusProvider;
import com.udacity.gradle.builditbigger.event.JokeLoadedEvent;
import com.udacity.gradle.builditbigger.logger.Debug;
import com.udacity.gradle.builditbigger.task.FetchJokeTask;
import com.udacity.gradle.builditbigger.utils.JokeConstants;


public class MainActivity extends ActionBarActivity {

    InterstitialAd mInterstitialAd;
    LinearLayout mProgressBar;
    String mJoke;
    boolean advShown;
    boolean forceShowJoke;
    boolean jokeRequested;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (LinearLayout) findViewById(R.id.progressLayout);
        if (savedInstanceState != null) {
            Debug.bundle(savedInstanceState);
            jokeRequested = savedInstanceState.getBoolean(JokeConstants.JOKE_REQUESTED,
                    false);
            forceShowJoke = savedInstanceState.getBoolean(JokeConstants.FORCE_SHOW_JOKE,
                    false);
            advShown = savedInstanceState.getBoolean(JokeConstants.ADV_SHOWN, false);
            mJoke = savedInstanceState.getString(JokeConstants.JOKE);
        }
        if (jokeRequested) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        BusProvider.getInstance().register(this);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstital_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Debug.e("Adv closed ", false);
                showJoke();
            }

            @Override
            public void onAdLoaded() {
                Debug.c();
                if (!advShown && jokeRequested) {
                    showAdv();
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Debug.e("Adv failed : " + errorCode, false);
                if (errorCode == 0) {
                    showJoke();
                } else {
                    forceShowJoke = true;
                }
            }
        });
        if (!jokeRequested) {
            requestNewInterstitial();
        }
    }


    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
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
        return super.onOptionsItemSelected(item);
    }

    public void showAdv() {
        if (mInterstitialAd.isLoaded()) {
            advShown = true;
            mInterstitialAd.show();
        }
    }

    public void tellJoke(View view) {
        Debug.c();
        if (!jokeRequested) {
            jokeRequested = true;
            mProgressBar.setVisibility(View.VISIBLE);
            advShown = false;
            showAdv();
            mJoke = null;
            new FetchJokeTask().execute();
        } else {
            Debug.showToastShort(getString(R.string.please_wait), this, true);
        }
    }

    @Subscribe
    public void jokeLoaded(JokeLoadedEvent event) {
        String joke = event.getJoke();
        Debug.i("Joke Loaded : " + joke, false);
        mJoke = joke;
        if (forceShowJoke) {
            showJoke();
        }
    }

    private void showJoke() {
        Debug.c();
        if (jokeRequested) {
            if (mJoke != null) {
                requestNewInterstitial();
                Debug.i("Going to show the Joke Loaded : " + mJoke, false);
                mProgressBar.setVisibility(View.GONE);
                Intent intent = new Intent(this, JokeDisplayActivity.class);
                intent.putExtra(JokeDisplayActivity.JOKE_DISPLAY_INTENT, mJoke);
                startActivity(intent);
                mJoke = null;
                advShown = true;
                forceShowJoke = false;
                jokeRequested = false;
            } else {
                //adv was closed but we dont have joke yet
                Debug.e("Joke not ready, but adv closed", false);
                forceShowJoke = true;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(JokeConstants.JOKE_REQUESTED, jokeRequested);
        savedInstanceState.putBoolean(JokeConstants.FORCE_SHOW_JOKE, forceShowJoke);
        savedInstanceState.putBoolean(JokeConstants.ADV_SHOWN, advShown);
        savedInstanceState.putString(JokeConstants.JOKE, mJoke);
        Debug.bundle(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }
}

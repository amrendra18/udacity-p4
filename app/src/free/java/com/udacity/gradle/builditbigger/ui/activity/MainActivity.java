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


public class MainActivity extends ActionBarActivity {

    InterstitialAd mInterstitialAd;
    LinearLayout mProgressBar;
    String mJoke;
    boolean advShown;
    boolean forceShowJoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (LinearLayout) findViewById(R.id.progressLayout);
        BusProvider.getInstance().register(this);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.banner_ad_unit_id));

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                Debug.e("Adv closed ", false);
                showJoke();
            }

            @Override
            public void onAdLoaded() {
                Debug.c();
                if (!advShown) {
                    showAdv();
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Debug.e("Adv failed : " + errorCode, false);
                showJoke();
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
        Debug.c();
        mProgressBar.setVisibility(View.VISIBLE);
        advShown = false;
        showAdv();
        mJoke = null;
        forceShowJoke = false;
        new FetchJokeTask().execute();
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
        } else {
            //adv was closed but we dont have joke yet
            Debug.e("Joke not ready, but adv closed", false);
            forceShowJoke = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }
}

/*

Handle cases:

1. Adv is loaded before
    a) user closes it before joke is loaded
    b) user closes it after joke is loaded

2. Joke is loaded, but adv is not loaded
 */

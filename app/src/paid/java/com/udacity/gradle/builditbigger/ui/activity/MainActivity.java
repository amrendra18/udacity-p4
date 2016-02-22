package com.udacity.gradle.builditbigger.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.amrendra.displaylibrary.JokeDisplayActivity;
import com.squareup.otto.Subscribe;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.bus.BusProvider;
import com.udacity.gradle.builditbigger.event.JokeLoadedEvent;
import com.udacity.gradle.builditbigger.logger.Debug;
import com.udacity.gradle.builditbigger.task.FetchJokeTask;


public class MainActivity extends ActionBarActivity {
    LinearLayout mProgressBar;
    boolean jokeRequested;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (LinearLayout) findViewById(R.id.progressLayout);
        BusProvider.getInstance().register(this);
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

    public void tellJoke(View view) {
        Debug.i("Joke Clicked", false);
        if (!jokeRequested) {
            mProgressBar.setVisibility(View.VISIBLE);
            jokeRequested = true;
            new FetchJokeTask().execute();
        } else {
            Debug.showToastShort(getString(R.string.please_wait), this, true);
        }
    }


    @Subscribe
    public void jokeLoaded(JokeLoadedEvent event) {
        jokeRequested = false;
        String joke = event.getJoke();
        Debug.i("Going to show the Joke Loaded : " + joke, false);
        mProgressBar.setVisibility(View.GONE);
        Intent intent = new Intent(this, JokeDisplayActivity.class);
        intent.putExtra(JokeDisplayActivity.JOKE_DISPLAY_INTENT, joke);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }
}

package com.udacity.gradle.builditbigger.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.amrendra.displaylibrary.JokeDisplayActivity;
import com.udacity.gradle.builditbigger.R;
import com.udacity.gradle.builditbigger.logger.Debug;
import com.udacity.gradle.builditbigger.task.FetchJokeTask;


public class MainActivity extends ActionBarActivity implements FetchJokeTask.JokeListener {
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
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
        mProgressBar.setVisibility(View.VISIBLE);
        new FetchJokeTask(this).execute();
    }


    @Override
    public void jokeLoaded(String joke) {
        Debug.i("Going to show the Joke Loaded : " + joke, false);
        mProgressBar.setVisibility(View.GONE);
        Intent intent = new Intent(this, JokeDisplayActivity.class);
        intent.putExtra(JokeDisplayActivity.JOKE_DISPLAY_INTENT, joke);
        startActivity(intent);
    }
}

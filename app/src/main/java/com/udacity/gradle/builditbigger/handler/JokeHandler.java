package com.udacity.gradle.builditbigger.handler;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ProgressBar;

import com.amrendra.displaylibrary.JokeDisplayActivity;
import com.udacity.gradle.builditbigger.task.Debug;
import com.udacity.gradle.builditbigger.task.FetchJokeTask;

/**
 * Created by Amrendra Kumar on 19/02/16.
 */
public class JokeHandler implements FetchJokeTask.JokeListener {

    Context mContext;
    ProgressBar mProgressBar;

    public JokeHandler(Context context, ProgressBar progressBar) {
        mContext = context;
        mProgressBar = progressBar;
    }

    @Override
    public void jokeLoaded(String joke) {
        Debug.i("Joke Loaded : " + joke, false);
        mProgressBar.setVisibility(View.GONE);
        Intent intent = new Intent(mContext, JokeDisplayActivity.class);
        intent.putExtra(JokeDisplayActivity.JOKE_DISPLAY_INTENT, joke);
        mContext.startActivity(intent);
    }
}

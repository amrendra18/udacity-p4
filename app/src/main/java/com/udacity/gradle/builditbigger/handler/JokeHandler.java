package com.udacity.gradle.builditbigger.handler;

import android.content.Context;
import android.content.Intent;

import com.amrendra.displaylibrary.JokeDisplayActivity;
import com.udacity.gradle.builditbigger.task.FetchJokeTask;

/**
 * Created by Amrendra Kumar on 19/02/16.
 */
public class JokeHandler implements FetchJokeTask.JokeListener {

    Context mContext;

    public JokeHandler(Context context) {
        mContext = context;
    }

    @Override
    public void jokeLoaded(String joke) {
        Intent intent = new Intent(mContext, JokeDisplayActivity.class);
        intent.putExtra(JokeDisplayActivity.JOKE_DISPLAY_INTENT, joke);
        mContext.startActivity(intent);
    }
}

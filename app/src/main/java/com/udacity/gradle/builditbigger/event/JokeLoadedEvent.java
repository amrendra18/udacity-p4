package com.udacity.gradle.builditbigger.event;

/**
 * Created by Amrendra Kumar on 22/02/16.
 */
public final class JokeLoadedEvent {
    String mJoke;

    public JokeLoadedEvent(String joke) {
        mJoke = joke;
    }

    public String getJoke() {
        return mJoke;
    }
}

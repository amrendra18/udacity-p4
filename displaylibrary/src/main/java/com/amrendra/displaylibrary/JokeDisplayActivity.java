package com.amrendra.displaylibrary;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class JokeDisplayActivity extends AppCompatActivity {

    public static final String JOKE_DISPLAY_INTENT = "joke_display_intent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_display);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            TextView jokesView = (TextView) findViewById(R.id.joke_display_tv);
            jokesView.setText(extras.getString(JOKE_DISPLAY_INTENT));
        }
    }
}

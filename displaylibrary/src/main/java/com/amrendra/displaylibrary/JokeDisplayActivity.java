package com.amrendra.displaylibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class JokeDisplayActivity extends AppCompatActivity {

    public static final String JOKE_DISPLAY_INTENT = "joke_display_intent";
    String mJoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_display);
        TextView jokesView = (TextView) findViewById(R.id.joke_display_tv);
        Button shareButton = (Button) findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareJoke();
            }
        });
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mJoke = extras.getString(JOKE_DISPLAY_INTENT);
            jokesView.setText(mJoke);
        }
    }

    private void shareJoke() {
        if (mJoke != null) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            shareIntent.setType("text/plain");
            String sharedJoke = "Wanna hear something funny.\n";
            sharedJoke += mJoke + "\n";
            sharedJoke += "Shared via #BuildItBigger\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, sharedJoke);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.action_share)));
        }
    }
}

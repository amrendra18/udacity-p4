package com.amrendra.displaylibrary.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.amrendra.displaylibrary.utils.FontsCache;

/**
 * Created by Amrendra Kumar on 23/02/16.
 */
public class JokeTextView extends TextView {

    public JokeTextView(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public JokeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public JokeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontsCache.getTypeface(context, FontsCache.JOKE_FONT);
        setTypeface(customFont);
    }
}

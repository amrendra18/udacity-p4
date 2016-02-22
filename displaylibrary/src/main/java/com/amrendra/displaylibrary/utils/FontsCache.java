package com.amrendra.displaylibrary.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;

/**
 * Created by Amrendra Kumar on 23/02/16.
 */
public class FontsCache {

    public static final String JOKE_FONT = "joke.ttf";

    private static HashMap<String, Typeface> fontCache = new HashMap<>();

    public static Typeface getTypeface(Context context, String fontname) {
        Typeface typeface = fontCache.get(fontname);
        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontname);
            } catch (Exception e) {
                return null;
            }
            fontCache.put(fontname, typeface);
        }

        return typeface;
    }

    public FontsCache() {
        throw new IllegalStateException("No instantiations please");
    }
}

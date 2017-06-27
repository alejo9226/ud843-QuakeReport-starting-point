package com.example.android.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by alejandroalfaro on 1/05/17.
 */

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {


    /** Tag for log messages */
    private static final String LOG_TAG = EarthquakeLoader.class.getName();
    /** Query URL */
    private String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    protected void onStartLoading(){
        Log.i(LOG_TAG, "You've entered the onStartLoading method");
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Earthquake> loadInBackground() {
        Log.i(LOG_TAG, "You've entered the loadInBackground method");
        if (mUrl == null) {
            return null;
        }
        List<Earthquake> result = QueryUtils.fetchEarthquakeData(mUrl);
        return result;
    }
}

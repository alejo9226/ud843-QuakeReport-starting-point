/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<Earthquake>> {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();

    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;


    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2017-05-10&endtime=2017-06-20&minmagnitude=5";

    private EarthquakeAdapter mAdapter;
    private TextView noResults;
    private ProgressBar mProgress;
    private Boolean isConnected;
    private NetworkInfo activeNetwork;
    private ConnectivityManager cm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        //Revisa con el ConnectivityManager si el celular esta conectado a internet
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetwork = cm.getActiveNetworkInfo();

        // Creo el adaptador para el listview de los terremotos.
        mAdapter = new EarthquakeAdapter(this, new ArrayList<Earthquake>());

        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        noResults = (TextView) findViewById(R.id.empty);
        mProgress = (ProgressBar) findViewById(R.id.loading_spinner);

        //Ato el adaptador al listView
        earthquakeListView.setAdapter(mAdapter);
        earthquakeListView.setEmptyView(noResults);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Earthquake earthquakeOnTap = mAdapter.getItem(i);

                Uri webpage = Uri.parse(earthquakeOnTap.getUrl());
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, webpage);

                // TODO - Start the chooser Activity, using the chooser intent
                startActivity(browserIntent);

            }
        });
        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        if (activeNetwork != null && activeNetwork.isConnected()) {
            //Se referencia la instancia del LoaderManager de la actividad para inicializarlo
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        }
        else {
            mProgress.setVisibility(View.GONE);
            noResults.setText(R.string.no_connection);
        }
        // Start the AsyncTask to fetch the earthquake data
        //EarthquakeAsyncTask task = new EarthquakeAsyncTask();
        //task.execute(USGS_REQUEST_URL);
    }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int i, Bundle bundle) {

        Log.i(LOG_TAG, "You've entered the method onCreateLoader()");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {

        Log.i(LOG_TAG, "You've entered the method onLoadFinished()");
        mProgress.setVisibility(View.GONE);
        noResults.setText(R.string.no_earthquakes);
        mAdapter.clear();
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {

            mAdapter.addAll(earthquakes);

        }
    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        Log.i(LOG_TAG, "You've entered the method onLoaderReset()");
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

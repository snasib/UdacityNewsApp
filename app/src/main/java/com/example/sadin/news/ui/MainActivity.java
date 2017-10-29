package com.example.sadin.news.ui;

import android.app.LoaderManager;
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
import android.widget.TextView;

import com.example.sadin.news.R;
import com.example.sadin.news.adapter.NewsArrayAdapter;
import com.example.sadin.news.loader.NewsLoader;
import com.example.sadin.news.model.News;
import com.example.sadin.news.model.Result;

import static android.view.View.GONE;

public class MainActivity
        extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<News> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int NEWS_LOADER_ID = 35;
    private static final String GUARDIAN_API_URL = "https://content.guardianapis.com/search";
    private static final String GUARDIAN_API_KEY = "";
    private static final String LIST_STATE = "listState";
    private NewsArrayAdapter mAdapter;
    private TextView mEmptyStateTextView;
    private ListView mListView;
    private LoaderManager mLoaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmptyStateTextView = findViewById(R.id.textEmpty);
        mEmptyStateTextView.setVisibility(GONE);

        mLoaderManager = getLoaderManager();

        View loadingIndicator = findViewById(R.id.progressBar);
        loadingIndicator.setVisibility(GONE);

        mAdapter = new NewsArrayAdapter(this);
        mListView = findViewById(R.id.listView);
        mListView.setAdapter(mAdapter);
        if (savedInstanceState != null
                && savedInstanceState.containsKey(LIST_STATE)) {
            mAdapter.addAll(savedInstanceState.<Result>getParcelableArrayList(LIST_STATE));
        }

        if (isNetworkAvailable()) {
            requestUpdate();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Result result = mAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(result.getWebUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(LIST_STATE, mAdapter.getListItems());
    }

    public void requestUpdate() {
        Log.i(TAG, "requestUpdate");
        if (mLoaderManager == null) {
            mLoaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            mLoaderManager.restartLoader(NEWS_LOADER_ID, null, this);
        }
    }

    public boolean isNetworkAvailable() {
        Log.i(TAG, "isNetworkAvailable");
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        View loadingIndicator = findViewById(R.id.progressBar);
        if (networkInfo != null && networkInfo.isConnected()) {
            loadingIndicator.setVisibility(View.VISIBLE);
            return true;
        } else {
            loadingIndicator.setVisibility(GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
            return false;
        }
    }

    @Override
    public Loader<News> onCreateLoader(int id, Bundle args) {
        Log.i(TAG, "onCreateLoader");

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String pageSize = sharedPrefs.getString(
                getString(R.string.settings_page_size_key),
                getString(R.string.settings_page_size_default));
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_ordering_key),
                getString(R.string.settings_ordering_default));
        String sectionId = sharedPrefs.getString(
                getString(R.string.settings_section_id_key),
                getString(R.string.settings_section_id_default));

        Uri baseUri = Uri.parse(GUARDIAN_API_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("api-key", GUARDIAN_API_KEY);
        uriBuilder.appendQueryParameter("page-size", pageSize);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        if (!sectionId.equals("")) {
            uriBuilder.appendQueryParameter("section", sectionId);
        }

        Log.i(TAG, uriBuilder.toString());

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<News> loader, News data) {
        Log.i(TAG, "onLoadFinished");
        View loadingIndicator = findViewById(R.id.progressBar);
        loadingIndicator.setVisibility(View.GONE);
        mAdapter.clear();
        if (data != null) {
            mEmptyStateTextView.setVisibility(View.GONE);
            mAdapter.addAll(data.getResponse().getResults());
        } else {
            mEmptyStateTextView.setText(R.string.no_updates_available);
        }
    }

    @Override
    public void onLoaderReset(Loader<News> loader) {
        Log.i(TAG, "onLoaderReset");
        mAdapter.clear();
    }
}

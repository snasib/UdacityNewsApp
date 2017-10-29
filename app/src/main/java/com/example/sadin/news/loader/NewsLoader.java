package com.example.sadin.news.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.sadin.news.model.News;

/**
 * Created by sadin on 26-Oct-17.
 */

public class NewsLoader extends AsyncTaskLoader<News> {
    private static final String TAG = NewsLoader.class.getSimpleName();
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(TAG, "onStartLoading");
        forceLoad();
    }

    @Override
    public News loadInBackground() {
        Log.i(TAG, "loadInBackground");
        if (mUrl == null) {
            return null;
        }
        return NewsHttpQuery.fetchNewsData(mUrl);
    }
}

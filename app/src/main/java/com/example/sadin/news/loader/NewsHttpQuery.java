package com.example.sadin.news.loader;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.sadin.news.model.News;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by sadin on 26-Oct-17.
 */

public final class NewsHttpQuery {
    private static final String TAG = NewsHttpQuery.class.getSimpleName();

    public static News fetchNewsData(String targetUrl) {
        Log.i(TAG, "fetchNewsData");
        URL url = buildUrl(targetUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(TAG, "fetchNewsData: ", e);
            e.printStackTrace();
        }
        return extractNewsFromJson(jsonResponse);
    }

    @Nullable
    private static News extractNewsFromJson(String jsonResponse) {
        Log.i(TAG, "extractFeatureFromJson");
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        News news = new News();

        try {
            Gson gson = new Gson();
            news = gson.fromJson(jsonResponse, News.class);
        } catch (JsonParseException e) {
            Log.e(TAG, "Error in parsing Json" + e.getMessage());
        }

        return news;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        Log.i(TAG, "makeHttpRequest");
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    @NonNull
    private static String readFromStream(InputStream inputStream) throws IOException {
        Log.i(TAG, "readFromStream");
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL buildUrl(String targetUrl) {
        Log.i(TAG, "buildUrl: " + targetUrl);
        URL url = null;
        try{
            url = new URL(targetUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "buildUrl: ", e);
            e.printStackTrace();
        }
        return url;
    }
}

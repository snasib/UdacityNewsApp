package com.example.sadin.news.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sadin.news.R;
import com.example.sadin.news.model.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sadin on 26-Oct-17.
 */

public class NewsArrayAdapter extends ArrayAdapter<Result> {
    private static final String TAG = NewsArrayAdapter.class.getSimpleName();
    private View mListView;
    public NewsArrayAdapter(@NonNull Context context, @NonNull List<Result> results) {
        super(context, 0, results);
    }

    public NewsArrayAdapter(@NonNull Context context) {
        super(context, 0);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.i(TAG, "getView");
        mListView = convertView;
        if (mListView == null) {
            mListView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_items,
                    parent,
                    false
            );
        }

        Result result = getItem(position);

        TextView webTitle = mListView.findViewById(R.id.textWebTitle);
        webTitle.setText(result.getWebTitle());
        TextView sectionName = mListView.findViewById(R.id.textSectionName);
        sectionName.setText(result.getSectionName());
        TextView author = mListView.findViewById(R.id.textAuthor);
        if (result.getAuthor() != null) {
            author.setText(result.getAuthor());
        } else {
            author.setVisibility(View.GONE);
        }
        TextView date = mListView.findViewById(R.id.textDate);
        date.setText(result.getWebPublicationDate());


        return mListView;
    }



    public ArrayList<Result> getListItems() {
        ArrayList<Result> objects = new ArrayList<>(getCount());
        for (int i = 0; i < getCount(); i++) {
            objects.add(getItem(i));
        }
        return objects;
    }
}

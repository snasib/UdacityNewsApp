package com.example.sadin.news.model;

import java.util.List;

/**
 * Created by sadin on 26-Oct-17.
 */

public class Response {
    private String status;
    private List<Result> results = null;

    public String getStatus() {
        return status;
    }

    public List<Result> getResults() {
        return results;
    }
}

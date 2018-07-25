package com.gz.jey.mynews.model;

import java.util.ArrayList;
import java.util.List;

public class NewsSection {

    private String status;
    private Integer numResults;
    private List<Result> results = new ArrayList<>();


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status =status ;
    }

    public Integer getNumResults() {
        return numResults;
    }

    public void setNumResults(Integer numResults) {
        this.numResults = numResults;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) { this.results = results; }
}


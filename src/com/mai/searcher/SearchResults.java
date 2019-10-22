package com.mai.searcher;

import java.util.ArrayList;
import java.util.List;

public class SearchResults {
    private List<SearchResult> results = new ArrayList<>();

    public void add(List<String> resultLines, int linesOffset, int indexOfResult) {
        results.add(new SearchResult(resultLines, linesOffset, indexOfResult));
    }

    public void add(SearchResults another) {
        results.addAll(another.results);
    }

    public List<SearchResult> get() {
        return results;
    }

    public int count() {
        return results.size();
    }
}

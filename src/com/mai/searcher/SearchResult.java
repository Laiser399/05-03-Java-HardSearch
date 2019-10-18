package com.mai.searcher;

public class SearchResult {
    private String found;
    private int index;

    public SearchResult(String found, int index) {
        this.found = found;
        this.index = index;
    }

    public String getFound() {
        return found;
    }

    public int getIndex() {
        return index;
    }
}

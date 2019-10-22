package com.mai.searcher;

import java.util.List;

public class SearchResult {
    private String[] linesBefore, linesAfter;
    private String result;
    private int lineOfResult;

    public SearchResult(List<String> found, int lineOffset, int indexOfResult) {
        linesBefore = new String[indexOfResult];
        found.subList(0, indexOfResult).toArray(linesBefore);
        result = found.get(indexOfResult);
        linesAfter = new String[found.size() - indexOfResult - 1];
        found.subList(indexOfResult + 1, found.size()).toArray(linesAfter);

        lineOfResult = lineOffset + indexOfResult;
    }

    public String[] getLinesBefore() {
        return linesBefore;
    }

    public String getResult() {
        return result;
    }

    public String[] getLinesAfter() {
        return linesAfter;
    }

    public int getLineOfResult() {
        return lineOfResult;
    }
}

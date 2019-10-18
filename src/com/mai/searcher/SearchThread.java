package com.mai.searcher;

import static java.lang.Math.min;
import static java.lang.Math.max;

public class SearchThread extends Thread {
    private StringBuilder content;
    private String searchString;
    private int startFrom, size, indent, offset;
    private SearchResults searchResults = new SearchResults();

    public SearchThread(StringBuilder content, String searchString, int startFrom, int size, int indent, int offset) {
        this.content = content;
        this.searchString = searchString;
        this.startFrom = startFrom;
        this.size = size;
        this.indent = indent;
        this.offset = offset;
    }

    @Override
    public void run() {
        if (startFrom >= content.length())
            return;
        String buf = content.substring(startFrom, min(content.length(), startFrom + size + searchString.length() - 1));

        int index;
        int start = 0;
        while ((index = buf.indexOf(searchString, start)) != -1) {
            int startIndex = max(0, startFrom + index - indent),
                endIndex = min(content.length(), startFrom + index + searchString.length() + indent);
            String result = content.substring(startIndex, endIndex);
            searchResults.add(result, offset + startIndex);

            start = index + searchString.length();
        }
    }

    public SearchResults getSearchResults() {
        if (isAlive())
            return null;
        return searchResults;
    }
}

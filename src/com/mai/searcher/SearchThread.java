package com.mai.searcher;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.min;
import static java.lang.Math.max;

public class SearchThread extends Thread {
    private List<String> lines;
    private String searchString;
    private int startFrom, count, indent, offset;
    private List<Match> matches = new ArrayList<>();

    public SearchThread(List<String> lines, String searchString, int startFrom, int count, int indent, int offset) {
        this.lines = lines;
        this.searchString = searchString;
        this.startFrom = startFrom;
        this.count = count;
        this.indent = indent;
        this.offset = offset;
    }

    @Override
    public void run() {
        for (int i = startFrom; i < min(lines.size(), startFrom + count); ++i) {
            String line = lines.get(i);
            if (line.contains(searchString)) {
                int firstIndex = max(0, i - indent),
                    endIndex = min(lines.size(), i + indent + 1);
                List<String> resultLines = lines.subList(firstIndex, endIndex);
                matches.add(new Match(resultLines,
                        offset + firstIndex, i - firstIndex));
            }
        }
    }

    public List<Match> getMatches() {
        if (isAlive())
            return null;
        return matches;
    }
}

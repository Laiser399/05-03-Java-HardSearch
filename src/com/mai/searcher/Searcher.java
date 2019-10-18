package com.mai.searcher;


import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Searcher {
    private static final int CHARS_FOR_THREAD = 10_000_000;
    private static final int THREAD_COUNT = 4;

    public void start() throws IOException, InterruptedException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            String filename = "D:\\_BIG_FILES_\\BigFileForJavaLab\\big_file.txt";
            String searchString = "терминал";
            int indent = 10;
            String saveFilename = "D:\\_BIG_FILES_\\BigFileForJavaLab\\result.txt";

//            System.out.print("Enter file name: ");
//            String filename = reader.readLine();
//            System.out.print("Enter search string: ");
//            String searchString = reader.readLine();
//            int indent = fetchCharsIndent(reader);
//            System.out.print("Save to: ");
//            String saveFilename = reader.readLine();


            long startTime = new Date().getTime();

            SearchResults results = search(filename, searchString, indent);
            saveResults(saveFilename, results, searchString, indent);

            System.out.println("Results saved to \"" + saveFilename + "\". Found " + results.count() + ".");
            System.out.println("Search ends in " + (new Date().getTime() - startTime) + "ms");

            return;
        }
    }

    private int fetchCharsIndent(BufferedReader reader) throws IOException {
        Integer indent = null;
        while (indent == null) {
            System.out.print("Enter chars indent: ");
            String indentStr = reader.readLine();
            try {
                indent = Integer.parseInt(indentStr);
                if (indent < 0) {
                    System.out.println("Wrong indent format (< 0).");
                    indent = null;
                }
            }
            catch (NumberFormatException e) {
                System.out.println("Wrong integer format.");
            }
        }

        return indent;
    }

    private SearchResults search(String filename, String searchString, int indent) throws IOException, InterruptedException {
        InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
        long fileSize = new File(filename).length();
        long startTime = new Date().getTime();

        SearchResults result = new SearchResults();

        int start = 0, offset = 0;
        int needToRead = CHARS_FOR_THREAD * THREAD_COUNT + searchString.length() - 1 + indent * 2;
        StringBuilder builder = new StringBuilder(needToRead + 100);
        while (reader.ready()) {
            readTo(reader, builder, needToRead);

            List<SearchThread> threads = new ArrayList<>();
            for (int i = 0; i < THREAD_COUNT; ++i) {
                SearchThread thread = new SearchThread(builder, searchString,
                        start + i * CHARS_FOR_THREAD, CHARS_FOR_THREAD, indent, offset);
                thread.start();
                threads.add(thread);
            }

            for (Thread thread : threads)
                thread.join();

            for (SearchThread thread : threads)
                result.add(thread.getSearchResults());

            int forDelete = Math.min(builder.length(), CHARS_FOR_THREAD * THREAD_COUNT - indent + start);
            offset += forDelete;
            start = indent;
            builder.delete(0, forDelete);

            updateStatus(new Date().getTime() - startTime,
                    (double) offset / fileSize, result.count());
        }

        reader.close();

        return result;
    }

    private void readTo(Reader reader, StringBuilder out, int minSize) throws IOException {
        char[] buf = new char[100];
        while (out.length() < minSize && reader.ready()) {
            int readCount = reader.read(buf);
            out.append(buf, 0, readCount);
        }
    }

    private void updateStatus(long timeRunning, double doneStatus, int countMatches) throws IOException {
        String doneStr = Double.toString(doneStatus * 100);
        if (doneStr.length() > 5)
            doneStr = doneStr.substring(0, 5);

        System.out.println("Running: " + timeRunning + "ms");
        System.out.println("Done: " + doneStr + "%");
        System.out.println("Count of matcher: " + countMatches);
    }

    private void saveResults(String filename, SearchResults results, String searchString, int indent) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write("Search for: ");
        writer.write(searchString);
        writer.write('\n');
        writer.write("Indent: ");
        writer.write(Integer.toString(indent));
        writer.write("\n\n");

        for (SearchResult result : results.get()) {
            writer.write("At:     ");
            writer.write(Integer.toString(result.getIndex()));
            writer.write('\n');
            writer.write("String: ");
            writer.write(result.getFound());
            writer.write("\n\n");
        }
        writer.close();
    }


    public static void main(String[] args) {
        Searcher searcher = new Searcher();
        try {
            searcher.start();
        }
        catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        }
        catch (InterruptedException e) {
            System.out.println("Thread error: " + e.getMessage());
        }
    }

}

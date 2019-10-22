package com.mai.generator;

import java.io.*;
import java.util.Date;
import java.util.Random;


public class Generator {
    private static final String filename = "D:\\_BIG_FILES_\\BigFileForJavaLab\\big_file2.txt";
    private static final int linesCount = 10_000;
    private static final float chanceToInsert = 0.002f;
    private static final String[] words = {
            "программа", "слово", "терминал", "процесс", "символ"
    };


    private Random random = new Random(123);//TODO remove seed
    private long timeCounter = 0;

    public void start() {
        try(FileWriter writer = new FileWriter(filename)) {
            for (int i = 0; i < linesCount; ++i) {
                writer.write(getNextLine());
                writer.write('\n');

                if (i % 100_000 == 0)
                    System.out.println("Done: " + ((double) i / linesCount));
            }
        }
        catch (IOException e) {
            System.out.println("IO error while write file \"" + filename + "\".");
        }
    }

    private String getNextLine() {
        String insertString = isInsertNow() ? getNextWord() : "<empty>";
        timeCounter += 1000;
        return new Date(timeCounter).toString() + " String: " + insertString;
    }

    private String getNextWord() {
        return words[random.nextInt(words.length)];
    }

    private boolean isInsertNow() {
        return random.nextFloat() < chanceToInsert;
    }




    public static void main(String[] args) {
        Generator generator = new Generator();
        generator.start();
    }
}











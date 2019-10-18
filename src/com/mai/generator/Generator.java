package com.mai.generator;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Generator {
    private static final String filename = "D:\\_BIG_FILES_\\BigFileForJavaLab\\big_file.txt",
                                wordsStatFilename = "D:\\_BIG_FILES_\\BigFileForJavaLab\\words.txt";
    private static final int fileSize = 2_000_000_000;
    private static final float chanceToRepeat = 0.80f;
    private static final int indent = 50;
    private static final String[] words = {
            "программа", "слово", "терминал", "процесс", "символ"
    };
    private static final String randomChars = "123098qwerty";

    private Random random = new Random(123);//TODO remove seed

    public void start() throws IOException, InterruptedException {
        deleteFile(filename);
        generateEmptyFile(filename);

        List<Integer> countOfRepeats = new ArrayList<>(words.length);
        for (int i = 0; i < words.length; ++i)
            countOfRepeats.add(0);

        RandomAccessFile file = new RandomAccessFile(filename, "rw");
        for (int i = 0; i < words.length; ++i) {
            String word = words[i];
            int count = 0;
            do {
                String insertString = getRandomString(indent) + word + getRandomString(indent);
                int pos = random.nextInt(fileSize - insertString.length());
                file.seek(pos);
                file.write(insertString.getBytes());
                count++;
            } while (isRepeatNow());
            countOfRepeats.set(i, count);
        }
        file.close();

        saveWordsStat(words, countOfRepeats);
    }

    private void deleteFile(String filename) {
        File file = new File(filename);
        if (file.exists())
            file.delete();
    }

    private void generateEmptyFile(String filename) throws IOException, InterruptedException {
        Process process = new ProcessBuilder("fsutil", "file",
                "createnew", filename, Long.toString(fileSize)).start();
        process.waitFor();
    }

    private boolean isRepeatNow() {
        return random.nextFloat() < chanceToRepeat;
    }

    private String getRandomString(int size) {
        StringBuilder builder = new StringBuilder(size);
        for (int i = 0; i < size; ++i) {
            int index = random.nextInt(randomChars.length());
            builder.append(randomChars.charAt(index));
        }
        return builder.toString();
    }

    private void saveWordsStat(String[] words, List<Integer> countOfRepeats) throws IOException {
        FileWriter writer = new FileWriter(wordsStatFilename);
        for (int i = 0; i < words.length; ++i) {
            writer.write(words[i] + " " + countOfRepeats.get(i) + "\n");
        }
        writer.close();
    }



    public static void main(String[] args) {
        Generator generator = new Generator();
        try {
            generator.start();
        }
        catch (IOException e) {
            System.out.println("IO error: " + e.getMessage());
        }
        catch (InterruptedException e) {
            System.out.println("Interrapted error: " + e.getMessage());
        }
    }
}

package methodB;

import utils.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ReaderThread extends Thread {
    private final List<String> fileNames;
    private final int startIndex;
    private final int step;
    private final MyQueue queue;

    public ReaderThread(List<String> fileNames, int startIndex, int step, MyQueue queue) {
        this.fileNames = fileNames;
        this.startIndex = startIndex;
        this.step = step;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = startIndex; i < fileNames.size(); i += step) {
                try (BufferedReader reader = new BufferedReader(new FileReader(fileNames.get(i)))){
                    String line;
                    while ((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        String id = parts[0].trim();
                        int score = Integer.parseInt(parts[1].trim());
                        queue.push(new Pair(id, score));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package methodB;

import utils.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReaderManager {
    private final MyQueue queue;
    private final List<String> fileNames;

    public ReaderManager(MyQueue queue, List<String> fileNames) {
        this.queue = queue;
        this.fileNames = fileNames;
    }

    public void run(int p_r) {
        ExecutorService executor = Executors.newFixedThreadPool(p_r);

        for (String fileName : fileNames) {
            executor.submit(() -> {
                try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                    String line;
                    while((line = reader.readLine()) != null) {
                        String[] parts = line.split(",");
                        String id = parts[0].trim();
                        int score = Integer.parseInt(parts[1].trim());
                        queue.push(new Pair(id, score));
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                System.err.println("Reader tasks did not finish in the expected time.");
            }
        } catch (InterruptedException e) {
            System.err.println("Reader manager interrupted.");
            Thread.currentThread().interrupt();
        }
    }
}

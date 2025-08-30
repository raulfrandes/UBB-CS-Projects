package methodA;

import utils.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class MethodA {
    private final List<String> fileNames;
    private final LinkedList list;

    public MethodA(final List<String> fileNames) {
        this.fileNames = fileNames;
        this.list = new LinkedList();
    }

    public void run() {
        try {
            for (String fileName : this.fileNames) {
                try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        String id = parts[0].trim();
                        int score = Integer.parseInt(parts[1].trim());
                        list.add(new Pair(id, score));
                    }
                }
            }
            list.saveInFile("Clasament_MethodA.txt");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

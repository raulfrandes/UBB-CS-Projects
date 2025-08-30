package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static boolean areResultFilesIdentical(String fileName) throws IOException {
        String sequentialResultFile = "Clasament_MethodA.txt";

        Map<String, Integer> methodAResults = parseResults(sequentialResultFile);
        Map<String, Integer> methodBResults = parseResults(fileName);

        return methodAResults.equals(methodBResults);
    }

    private static Map<String, Integer> parseResults(String fileName) throws IOException {
        Map<String, Integer> results = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String id = parts[0].trim();
                    int score = Integer.parseInt(parts[1].trim());
                    results.put(id, score);
                }
            }
        }
        return results;
    }
}

package utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Utils {
    public static void generateFiles() throws IOException {
        Random random = new Random();

        for (int country = 1; country <= 5; country++) {
            int numContestants = 80 + random.nextInt(21);
            String countryCode = "C" + country;

            for (int problem = 1; problem <= 10; problem++) {
                String fileName = "Scores/Result" + countryCode + "_P" + problem + ".txt";
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                    for (int contestant = 1; contestant <= numContestants; contestant++) {
                        String id = countryCode + contestant;
                        double chance = random.nextDouble();

                        if (chance < 0.02) {
                            writer.write(id + ",-1\n");
                        } else if (chance < 0.12) {
                            continue;
                        } else {
                            int score = random.nextInt(11);
                            if (score == 0) {
                                continue;
                            }
                            writer.write(id + "," + score + "\n");
                        }
                    }
                }
            }
        }
    }

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

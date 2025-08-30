package com.myapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 1) {
            System.out.println("Usage: java Client <ClientID>");
            return;
        }

        String clientId = args[0];
        File directory = new File("src/main/resources/");
        System.out.println(clientId);
        File[] files = directory.listFiles((dir, name) -> name.startsWith("Rezultate" + clientId));

        if (files != null && files.length > 0) {
            System.out.println("Files for client " + clientId + ":");
            for (File file : files) {
                System.out.println(file.getName());
            }
        } else {
            System.out.println("No files found for client " + clientId);
        }

        String serverUrl = "http://192.168.238.14:8080/contest";
        Integer deltaX = 2000; // Interval between requests in milliseconds
        List<Pair> results = readResultsFromFiles(files);

        List<Map<String, Integer>> batches = splitIntoBatches(results, 20);

        for (int i = 0; i < batches.size(); i++) {
            System.out.println("Sending batch " + (i + 1) + " of " + batches.size());
            sendResults(serverUrl + "/submit?countryId=" + clientId, batches.get(i));
            Thread.sleep(deltaX); // Wait before sending the next batch
        }

        Map<String, Integer> rankingResponse = sendGetRankingRequest(serverUrl + "/ranking?countryId=" + clientId);
        System.out.println("Country Ranking: " + rankingResponse);

        byte[] zipContent = sendGetRequest(serverUrl + "/final-results");
        extractAndSaveFiles(zipContent, Paths.get("src/main/resources/Results"));

    }

    private static List<Pair> readResultsFromFiles(File[] files) throws IOException {
        List<Pair> results = new ArrayList<>();

        for (File file : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    // Split the line into participant_id, score, and country_id
                    String[] parts = line.split(",");
                    if (parts.length != 3) {
                        System.out.println("Skipping invalid line: " + line);
                        continue;
                    }

                    String participantId = parts[0].trim();
                    int score;
                    try {
                        score = Integer.parseInt(parts[1].trim());
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid score in line: " + line);
                        continue;
                    }
                    Pair result = new Pair(participantId, score);
                    results.add(result);
                }
            }
        }

        return results;
    }

    private static List<Map<String, Integer>> splitIntoBatches(List<Pair> results, int batchSize) {
        List<Map<String, Integer>> batches = new ArrayList<>();
        Map<String, Integer> currentBatch = new LinkedHashMap<>();

        int count = 0;
        for (Pair entry : results) {
            currentBatch.put(entry.id, entry.score);
            count++;
            if (count == batchSize) {
                batches.add(new LinkedHashMap<>(currentBatch));
                currentBatch.clear();
                count = 0;
            }
        }

        // Add the remaining entries as the last batch
        if (!currentBatch.isEmpty()) {
            batches.add(currentBatch);
        }

        return batches;
    }

    private static void sendResults(String url, Map<String, Integer> results) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setConnectTimeout(30000);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(new ObjectMapper().writeValueAsString(results).getBytes());
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("Results submitted successfully.");
        } else {
            System.out.println("Failed to submit results. HTTP Code: " + responseCode);
        }
        conn.disconnect();
    }

    private static byte[] sendGetRequest(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(30000);

        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            return outputStream.toByteArray();
        }
    }


    private static void extractAndSaveFiles(byte[] zipContent, Path outputDir) throws IOException {
        // Ensure the output directory exists
        Files.createDirectories(outputDir);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(zipContent);
             ZipInputStream zis = new ZipInputStream(bais)) {

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                // Skip directories in the ZIP
                if (entry.isDirectory()) {
                    continue;
                }

                // Determine the output file path
                Path outputPath = outputDir.resolve(entry.getName());

                // Write the file content to disk
                try (OutputStream os = Files.newOutputStream(outputPath)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = zis.read(buffer)) != -1) {
                        os.write(buffer, 0, bytesRead);
                    }
                }

                System.out.println("Saved file: " + outputPath);
            }
        }
    }

    private static Map<String, Integer> sendGetRankingRequest(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(30000);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Integer> resultMap;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            resultMap = mapper.readValue(reader, new TypeReference<Map<String, Integer>>(){});
        } finally {
            conn.disconnect();
        }

        return resultMap;
    }
}
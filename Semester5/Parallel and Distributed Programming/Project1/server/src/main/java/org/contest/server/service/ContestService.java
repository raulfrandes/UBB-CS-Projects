package org.contest.server.service;

import org.contest.server.util.*;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ContestService {
    private final int p_r = 4;
    private final int p_w = 8;
    private final int queueCapacity = 50;
    private final long deltaT = 4;
    private final MyQueue queue;
    private final ThreadSafeLinkedList list;
    private final ExecutorService readerThreadPool;
    private final List<Thread> workerThreads = new ArrayList<>();
    private final Set<String> activeClients = ConcurrentHashMap.newKeySet();
    private final AtomicInteger activeClientsCount = new AtomicInteger(0);
    private final AtomicInteger activeFinalResultsRequests = new AtomicInteger(0);
    private volatile boolean isFinalized = false;

    private volatile long startTime = 0;
    private volatile long endTime = 0;

    private Future<Map<String, Integer>> cachedCountryRankingFuture = null;
    private long lastRankingComputationTime = 0;

    public ContestService() {
        queue = new MyQueue(queueCapacity);
        list = new ThreadSafeLinkedList();
        Set<Id> blackList = new HashSet<>();

        readerThreadPool = Executors.newFixedThreadPool(p_r);

        for (int i = 0; i < p_w; i++) {
            Thread worker = new WorkerThread(queue, list, blackList);
            workerThreads.add(worker);
            worker.start();
        }

        LogService.log("Server initializat cu " + p_r + " reader threads si " + p_w + " worker threads.");
    }

    public void processResults(Map<String, Integer> results, String countryId) {
        if (startTime == 0) {
            startTime = System.nanoTime();
        }

        LogService.log("Am primit rezultatele de la tara: " + countryId);
        isFinalized = false;

        activeClients.add(countryId);
        activeClientsCount.set(activeClients.size());

        readerThreadPool.submit(() -> {
            for (Map.Entry<String, Integer> entry : results.entrySet()) {
                try {
                    Triplet result = new Triplet(countryId, entry.getKey(), entry.getValue());
                    queue.push(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            LogService.log("S-a procesat blocul de rezultate de la tara " + countryId + ".");
        });
    }

    public Map<String, Integer> getCountryRanking(String countryId) {
        LogService.log(countryId + " a cerut clasamentul pe tari.");

        activeClients.remove(countryId);
        int remainingClients = activeClientsCount.decrementAndGet();
        LogService.log("Tara " + countryId + " a terminat trimiterea datelor. Clienți activi rămași: " + remainingClients);

        long currentTime = System.currentTimeMillis();

        if (cachedCountryRankingFuture != null && currentTime - lastRankingComputationTime < deltaT) {
            try {
                return cachedCountryRankingFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        cachedCountryRankingFuture = singleThreadExecutor.submit(() -> {
            LogService.log("Se calculeaza clasamentul pe tari...");
            return list.getCountryRanking();
        });

        lastRankingComputationTime = currentTime;

        try {
            Map<String, Integer> ranking = cachedCountryRankingFuture.get();
            singleThreadExecutor.shutdown();
            return ranking;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return Collections.emptyMap();
        }
    }

    public byte[] getFinalResults() throws IOException {
        LogService.log("S-au cerut clasamentele finale.");

        activeFinalResultsRequests.incrementAndGet();

        while (activeClientsCount.get() > 0) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                LogService.log("Eroare la așteptarea finalizării: " + e.getMessage());
                return null;
            }
        }
        LogService.log("Toate tarile au terminat trimiterea datelor.");

        if (!isFinalized) {
            finalizeLeaderboard();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
            zos.putNextEntry(new ZipEntry("ClasamentFinal.txt"));
            byte[] leaderboardContent = Files.readAllBytes(Path.of("src/main/resources/results/ClasamentFinal.txt"));
            zos.write(leaderboardContent);
            zos.closeEntry();

            zos.putNextEntry(new ZipEntry("ClasamentTari.txt"));
            byte[] rankingContent = Files.readAllBytes(Path.of("src/main/resources/results/ClasamentTari.txt"));
            zos.write(rankingContent);
            zos.closeEntry();
        }

        LogService.log("S-au trimis clasamentele finale.");

        int remainingFinalResultsRequests = activeFinalResultsRequests.decrementAndGet();
        if (remainingFinalResultsRequests == 0) {
            if (endTime == 0) {
                endTime = System.nanoTime();
                System.out.println((endTime - startTime) / 1000000f);
                try {
                    if (areResultFilesIdentical("src/main/resources/results/ClasamentFinal.txt")) {
                        System.out.println("Identical");
                    } else {
                        System.out.println("Not Identical");
                    }
                } catch (IOException e) {
                    LogService.log("Eroare la compararea rezultatelor (secvential vs. paralel): " + e.getMessage());
                }
            }
        }

        return baos.toByteArray();
    }

    private void finalizeLeaderboard() {
        try {
            LogService.log("Finalizarea clasamentelor incepe...");

            readerThreadPool.shutdown();
            queue.finish();

            for (Thread worker : workerThreads) {
                worker.join();
            }

            list.saveInFile("src/main/resources/results/ClasamentFinal.txt");
            LogService.log("Fisierul ClasamentFinal.txt a fost creat.");
            list.saveCountryRanking("src/main/resources/results/ClasamentTari.txt");
            LogService.log("Fisierul ClasamentTari.txt a fost creat.");

            isFinalized = true;
        } catch (InterruptedException | IOException e) {
            LogService.log("Eroare la finalizarea clasamentelor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean areResultFilesIdentical(String fileName) throws IOException {
        String sequentialResultFile = "src/main/resources/results/ClasamentSecvential.txt";

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

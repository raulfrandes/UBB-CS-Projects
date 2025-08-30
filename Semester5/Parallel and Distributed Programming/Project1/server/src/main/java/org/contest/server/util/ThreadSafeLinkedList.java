package org.contest.server.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeLinkedList {
    private final Node head;
    private final Node tail;

    public ThreadSafeLinkedList() {
        this.head = new Node(null, null, 0);
        this.tail = new Node(null, null, 0);
        head.next = tail;
    }

    static class Node {
        String countryId;
        String contestantId;
        int score;
        Node next;
        final Lock lock = new ReentrantLock();

        public Node(String countryId, String contestantId, int score) {
            this.countryId = countryId;
            this.contestantId = contestantId;
            this.score = score;
            this.next = null;
        }
    }

    public void add(Triplet triplet) {
        String countryId = triplet.countryId;
        String contestantId = triplet.contestantId;
        int score = triplet.score;

        Node prev = head;
        prev.lock.lock();
        try {
            Node current = prev.next;

            while (current != tail) {
                current.lock.lock();
                try {
                    if (current.contestantId.equals(contestantId) && current.countryId.equals(countryId)) {
                        if (score == -1) {
                            prev.next = current.next;
                        } else {
                            current.score += score;
                        }
                        return;
                    }
                } finally {
                    prev.lock.unlock();
                    prev = current;
                }
                current = current.next;
            }

            if (score != -1) {
                Node newNode = new Node(countryId, contestantId, score);
                prev.next = newNode;
                newNode.next = tail;
            }
        } finally {
            prev.lock.unlock();
        }
    }

    public Map<String, Integer> getCountryRanking() {
        Map<String, Integer> countryScores = new HashMap<>();
        Node current = head.next;

        System.out.println("start iteration");

        while (current != tail) {
            current.lock.lock();
            try {
                countryScores.put(current.countryId, countryScores.getOrDefault(current.countryId, 0) + current.score);
            } finally {
                current.lock.unlock();
            }
            current = current.next;
        }

        System.out.println("end iteration");

        Map<String, Integer> sortedCountryScores = new LinkedHashMap<>();
        countryScores.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .forEachOrdered(entry -> sortedCountryScores.put(entry.getKey(), entry.getValue()));

        System.out.println("sorted country scores: " + sortedCountryScores);

        return sortedCountryScores;
    }

    public void saveCountryRanking(String filename) throws IOException {
        System.out.println("Saving country ranking to: " + filename);
        Map<String, Integer> countryRanking = getCountryRanking();

        System.out.println("Country ranking: ");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Map.Entry<String, Integer> entry : countryRanking.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
                writer.write(entry.getKey() + "," + entry.getValue() + "\n");
            }
        }
    }

    public void remove(String countryId, String contestantId) {
        Node prev = head;
        prev.lock.lock();
        try {
            Node current = prev.next;

            while (current != tail) {
                current.lock.lock();
                try {
                    if (current.countryId.equals(countryId) && current.contestantId.equals(contestantId)) {
                        prev.next = current.next;
                        return;
                    }
                } finally {
                    prev.lock.unlock();
                    prev = current;
                }
                current = current.next;
            }
        } finally {
            prev.lock.unlock();
        }
    }

    public void sort() {
        Node sorted = null;
        Node current = head.next;

        while (current != null) {
            Node next = current.next;
            current.next = null;
            sorted = insertSorted(sorted, current);
            current = next;
        }

        head.next = sorted;
    }

    private Node insertSorted(Node sorted, Node newNode) {
        if (sorted == null || newNode.score > sorted.score) {
            newNode.next = sorted;
            return newNode;
        }

        Node current = sorted;
        while (current.next != null && current.next.score >= newNode.score) {
            current = current.next;
        }
        newNode.next = current.next;
        current.next = newNode;

        return sorted;
    }

    public void saveInFile(String filename) throws IOException {
        sort();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            Node current = head.next;
            while (current != tail) {
                writer.write(current.countryId + "," + current.contestantId + "," + current.score + "\n");
                current = current.next;
            }
        }
    }
}

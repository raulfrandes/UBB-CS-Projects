package methodA;

import utils.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LinkedList {
    private final Node head;
    private final Node tail;
    private final Set<String> blacklist;

    public LinkedList() {
        this.head = new Node(null, 0);
        this.tail = new Node(null, 0);
        head.next = tail;
        blacklist = new HashSet<String>();
    }

    static class Node {
        String id;
        int score;
        Node next;

        public Node(String id, int score) {
            this.id = id;
            this.score = score;
            this.next = null;
        }
    }

    public void add(Pair pair) {
        String id = pair.id;
        int score = pair.score;

        if (blacklist.contains(id)) {
            return;
        }

        Node prev = head;
        Node current = prev.next;
        while (current != tail && !current.id.equals(id)) {
            prev = current;
            current = current.next;
        }

        if (current != tail) {
            if (score == -1) {
                prev.next = current.next;
                blacklist.add(id);
            } else {
                current.score += score;
            }
        } else {
            if (score == -1) {
                blacklist.add(id);
            } else {
                Node newNode = new Node(id, score);
                newNode.next = current;
                prev.next = newNode;
            }
        }
    }

    public void sort() {
        Node sorted = null;
        Node current = head.next;

        while (current != tail) {
            Node next = current.next;
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
            int i = 1;
            while (current != null) {
                writer.write(current.id + "," + current.score + "\n");
                current = current.next;
            }
        }
    }
}

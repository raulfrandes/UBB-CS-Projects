package methodA;

import utils.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class LinkedList {
    private Node head;
    private final Set<String> blackList;

    public LinkedList() {
        head = null;
        blackList = new HashSet<>();
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

        if (blackList.contains(id)) {
            return;
        }

        Node current = head;
        Node prev = null;
        while (current != null && !current.id.equals(id)) {
            prev = current;
            current = current.next;
        }

        if (current != null) {
            if (score == -1) {
                delete(prev, current);
                blackList.add(id);
            } else {
                current.score += score;
                if (prev != null && current.score > prev.score) {
                    reorder(current, prev);
                }
            }
        } else {
            if (score == -1) {
                blackList.add(id);
            } else {
                Node newNode = new Node(id, score);
                insertInList(newNode);
            }
        }
    }

    private void delete(Node prev, Node current) {
        if (prev == null) {
            head = current.next;
        } else {
            prev.next = current.next;
        }
    }

    private void reorder(Node node, Node prev) {
        if (prev != null) {
            prev.next = node.next;
        } else {
            head = node.next;
        }
        insertInList(node);
    }

    private void insertInList(Node newNode) {
        if (head == null || newNode.score > head.score) {
            newNode.next = head;
            head = newNode;
        } else {
            Node current = head;
            while (current.next != null && current.next.score >= newNode.score) {
                current = current.next;
            }
            newNode.next = current.next;
            current.next = newNode;
        }
    }

    public void saveInFile(String filename) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            Node current = head;
            while (current != null) {
                writer.write(current.id + "," + current.score + "\n");
                current = current.next;
            }
        }
    }
}

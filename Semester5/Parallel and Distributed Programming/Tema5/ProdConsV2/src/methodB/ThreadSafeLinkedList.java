package methodB;

import utils.Pair;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadSafeLinkedList {
    private final Node head;
    private final Node tail;

    public ThreadSafeLinkedList() {
        this.head = new Node(null, 0);
        this.tail = new Node(null, 0);
        head.next = tail;
    }

    static class Node {
        String id;
        int score;
        Node next;
        final Lock lock = new ReentrantLock();

        public Node(String id, int score) {
            this.id = id;
            this.score = score;
            this.next = null;
        }
    }

    public void add(Pair pair) {
        String id = pair.id;
        int score = pair.score;

        Node prev = head;
        prev.lock.lock();
        try {
            Node current = prev.next;

            while (current != tail) {
                current.lock.lock();
                try {
                    if (current.id.equals(id)) {
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
                Node newNode = new Node(id, score);
                prev.next = newNode;
                newNode.next = tail;
            }
        } finally {
            prev.lock.unlock();
        }
    }

    public void remove(String id) {
        Node prev = head;
        prev.lock.lock();
        try {
            Node current = prev.next;

            while (current != tail) {
                current.lock.lock();
                try {
                    if (current.id.equals(id)) {
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

        while (current != tail) {
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
            while (current != null) {
                writer.write(current.id + "," + current.score + "\n");
                current = current.next;
            }
        }
    }
}

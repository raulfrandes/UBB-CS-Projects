package org.contest.server.util;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyQueue {
    private final LinkedList<Triplet> list = new LinkedList<>();
    private final int capacity;
    private boolean done = false;

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public MyQueue(int capacity) {
        this.capacity = capacity;
    }

    public void push(Triplet triplet) throws InterruptedException {
        lock.lock();
        try {
            while (list.size() >= capacity) {
                notFull.await();
            }
            list.addLast(triplet);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Triplet pop() throws InterruptedException {
        lock.lock();
        try {
            while (list.isEmpty() && !done) {
                notEmpty.await();
            }
            if (list.isEmpty()) {
                return null;
            }
            Triplet triplet = list.removeFirst();
            notFull.signal();
            return triplet;
        } finally {
            lock.unlock();
        }
    }

    public void finish() {
        lock.lock();
        try {
            done = true;
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }
}

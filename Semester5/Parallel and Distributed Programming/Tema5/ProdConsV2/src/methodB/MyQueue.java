package methodB;

import utils.Pair;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyQueue {
    private final LinkedList<Pair> list = new LinkedList<>();
    private final int capacity;
    private boolean done = false;

    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public MyQueue(int capacity) {
        this.capacity = capacity;
    }

    public void push(Pair pair) throws InterruptedException {
        lock.lock();
        try {
            while (list.size() >= capacity) {
                notFull.await();
            }
            list.addLast(pair);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Pair pop() throws InterruptedException {
        lock.lock();
        try {
            while (list.isEmpty() && !done) {
                notEmpty.await();
            }
            if (list.isEmpty()) {
                return null;
            }
            Pair pair = list.removeFirst();
            notFull.signal();
            return pair;
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

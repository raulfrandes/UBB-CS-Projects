package methodB;

import utils.Pair;

import java.util.LinkedList;

public class MyQueue {
    private final LinkedList<Pair> list = new LinkedList<>();
    private boolean done = false;

    public synchronized void push(Pair pair) {
        list.add(pair);
        notifyAll();
    }

    public synchronized Pair pop() throws InterruptedException {
        while (list.isEmpty() && !done) {
            wait();
        }
        return list.isEmpty() ? null : list.removeFirst();
    }

    public synchronized void finish() {
        done = true;
        notifyAll();
    }
}

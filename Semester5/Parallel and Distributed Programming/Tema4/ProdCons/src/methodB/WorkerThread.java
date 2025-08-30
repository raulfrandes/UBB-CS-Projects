package methodB;

import utils.Pair;

public class WorkerThread extends Thread {
    private final MyQueue queue;
    private final ThreadSafeLinkedList list;

    public WorkerThread(MyQueue queue, ThreadSafeLinkedList list) {
        this.queue = queue;
        this.list = list;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Pair pair = queue.pop();
                if (pair == null) {
                    break;
                }
                list.add(pair);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package methodB;

import utils.Pair;

import java.util.Set;

public class WorkerThread extends Thread {
    private final MyQueue queue;
    private final ThreadSafeLinkedList list;
    private final Set<String> blacklist;

    public WorkerThread(MyQueue queue, ThreadSafeLinkedList list, Set<String> blacklist) {
        this.queue = queue;
        this.list = list;
        this.blacklist = blacklist;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Pair pair = queue.pop();
                if (pair == null) {
                    break;
                }

                synchronized (blacklist) {
                    if (blacklist.contains(pair.id)) {
                        continue;
                    }
                    if (pair.score == -1) {
                        blacklist.add(pair.id);
                        list.remove(pair.id);
                        continue;
                    }
                }

                list.add(pair);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

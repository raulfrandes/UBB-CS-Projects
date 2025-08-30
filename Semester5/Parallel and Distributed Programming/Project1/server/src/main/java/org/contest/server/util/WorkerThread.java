package org.contest.server.util;

import java.util.Set;

public class WorkerThread extends Thread {
    private final MyQueue queue;
    private final ThreadSafeLinkedList list;
    private final Set<Id> blacklist;

    public WorkerThread(MyQueue queue, ThreadSafeLinkedList list, Set<Id> blacklist) {
        this.queue = queue;
        this.list = list;
        this.blacklist = blacklist;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Triplet triplet = queue.pop();
                if (triplet == null) {
                    break;
                }

                synchronized (blacklist) {
                    Id id = new Id(triplet.countryId, triplet.contestantId);
                    if (blacklist.contains(id)) {
                        continue;
                    }
                    if (triplet.score == -1) {
                        blacklist.add(id);
                        list.remove(triplet.countryId, triplet.contestantId);
                        continue;
                    }
                }

                list.add(triplet);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

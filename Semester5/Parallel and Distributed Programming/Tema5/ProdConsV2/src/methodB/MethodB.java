package methodB;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MethodB {
    private final List<String> fileNames;
    private final int p_r;
    private final int p_w;
    private final MyQueue queue;
    private final ThreadSafeLinkedList list;
    private final Set<String> blackList;

    public MethodB(List<String> fileNames, int p_r, int p_w, int queueCapacity) {
        this.fileNames = fileNames;
        this.p_r = p_r;
        this.p_w = p_w;
        this.queue = new MyQueue(queueCapacity);
        this.list = new ThreadSafeLinkedList();
        this.blackList = new HashSet<>();
    }

    public void run() {
        ReaderManager readerManager = new ReaderManager(queue, fileNames);
        Thread readerManagerThread = new Thread(() -> readerManager.run(p_r));

        Thread[] workers = new Thread[p_w];
        for (int i = 0; i < p_w; i++) {
            workers[i] = new WorkerThread(queue, list, blackList);
            workers[i].start();
        }

        readerManagerThread.start();

        try {
            readerManagerThread.join();
            queue.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            list.saveInFile("Clasament_MethodB.txt");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}

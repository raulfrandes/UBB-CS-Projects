package methodB;

import java.io.IOException;
import java.util.List;

public class MethodB {
    private final List<String> fileNames;
    private final int p_r;
    private final int p_w;
    private final MyQueue queue;
    private final ThreadSafeLinkedList list;

    public MethodB(List<String> fileNames, int p_r, int p_w) {
        this.fileNames = fileNames;
        this.p_r = p_r;
        this.p_w = p_w;
        this.queue = new MyQueue();
        this.list = new ThreadSafeLinkedList();
    }

    public void run() {
        Thread[] readers = new Thread[p_r];
        for (int i = 0; i < p_r; i++) {
            readers[i] = new ReaderThread(fileNames, i, p_r, queue);
            readers[i].start();
        }

        Thread[] workers = new Thread[p_w];
        for (int i = 0; i < p_w; i++) {
            workers[i] = new WorkerThread(queue, list);
            workers[i].start();
        }

        for (Thread reader : readers) {
            try {
                reader.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        queue.finish();

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

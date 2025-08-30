package methodB;

import methodA.LinkedList;
import utils.Pair;

import java.io.IOException;

public class ThreadSafeLinkedList extends LinkedList {
    @Override
    public synchronized void add(Pair pair) {
        super.add(pair);
    }

    @Override
    public synchronized void saveInFile(String filename) throws IOException {
        super.saveInFile(filename);
    }
}

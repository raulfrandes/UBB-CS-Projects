import java.util.concurrent.CyclicBarrier;

public class RowsMethod {
    private final int[][] matrix;
    private final int[][] kernel;
    private final int N;
    private final int M;
    private final int p;
    private final CyclicBarrier barrier;

    public RowsMethod(int[][] matrix, int[][] kernel, int p) {
        this.matrix = matrix;
        this.kernel = kernel;
        this.N = matrix.length;
        this.M = matrix[0].length;
        this.p = p;
        this.barrier = new CyclicBarrier(p);
    }

    public void start() {
        Thread[] threads = new RowsThread[p];

        int start = 0;
        int end = N / p;
        int rest = N % p;

        for (int i = 0; i < p; i++) {
            if (rest > 0) {
                end++;
                rest--;
            }
            threads[i] = new RowsThread(matrix, kernel, start, end, barrier);
            threads[i].start();

            start = end;
            end = start + N / p;
        }

        for (int i = 0; i < p; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

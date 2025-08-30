import java.util.concurrent.CyclicBarrier;

public class RowsThread extends Thread {
    private final int[][] matrix;
    private final int[][] kernel;

    private final int N;
    private final int M;
    private final int start;
    private final int end;
    private final CyclicBarrier barrier;

    RowsThread(int[][] matrix, int[][] kernel, int start, int end, CyclicBarrier barrier) {
        this.matrix = matrix;
        this.kernel = kernel;
        this.start = start;
        this.end = end;
        N = matrix.length;
        M = matrix[0].length;
        this.barrier = barrier;
    }

    public void run() {
        int[] previousRow = new int[M];
        int[] currentRow = new int[M];
        int[] nextRow = new int[M];
        int[] endRow = new int[M];

        if (start > 0) {
            System.arraycopy(matrix[start - 1], 0, previousRow, 0, M);
        } else {
            System.arraycopy(matrix[0], 0, previousRow, 0, M);
        }

        System.arraycopy(matrix[start], 0, currentRow, 0, M);

        if (end < N - 1) {
            System.arraycopy(matrix[end], 0, endRow, 0, M);
            System.arraycopy(matrix[start + 1], 0, nextRow, 0, M);
        } else {
            System.arraycopy(matrix[N - 1], 0, endRow, 0, M);
            System.arraycopy(matrix[N - 1], 0, nextRow, 0, M);
        }

        try {
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = start; i < end; i++) {
            if (i > start) {
                System.arraycopy(matrix[i], 0, currentRow, 0, M);
            }

            if (i < end - 1) {
                System.arraycopy(matrix[i + 1], 0, nextRow, 0, M);
            }
            else {
                System.arraycopy(endRow, 0, nextRow, 0, M);
            }

            for (int j = 0; j < M; j++) {
                matrix[i][j] = Utils.convolveAt(previousRow, currentRow, nextRow, kernel, i, j, N ,M, kernel.length);
            }

            System.arraycopy(currentRow, 0, previousRow, 0, M);
        }
    }
}

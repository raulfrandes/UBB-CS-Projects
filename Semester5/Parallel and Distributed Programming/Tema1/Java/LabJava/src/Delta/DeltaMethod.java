package Delta;

import Columns.ColumnsThread;

public class DeltaMethod {
    private final int[][] matrix;
    private final int[][] kernel;
    private final int N;
    private final int M;
    private final int p;

    public DeltaMethod(int[][] matrix, int[][]kernel, int p) {
        this.matrix = matrix;
        this.kernel = kernel;
        this.N = matrix.length;
        this.M = matrix[0].length;
        this.p = p;
    }

    public int[][] start() {
        Thread[] threads = new DeltaThread[p];

        int totalElements = N * M;

        int start = 0;
        int end = totalElements / p;
        int rest = totalElements % p;

        int[][] result = new int[N][M];

        for (int i = 0; i < p; i++) {
            if (rest > 0) {
                end++;
                rest--;
            }
            threads[i] = new DeltaThread(matrix, kernel, result, start, end);
            threads[i].start();

            start = end;
            end = start + totalElements / p;
        }

        for (int i = 0; i < p; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return result;
    }
}

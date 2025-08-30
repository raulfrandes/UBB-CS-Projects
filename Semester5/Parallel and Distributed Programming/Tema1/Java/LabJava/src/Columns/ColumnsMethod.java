package Columns;

import Rows.RowsThread;

public class ColumnsMethod {
    private final int[][] matrix;
    private final int[][] kernel;
    private final int N;
    private final int M;
    private final int p;

    public ColumnsMethod(int[][] matrix, int[][] kernel, int p) {
        this.matrix = matrix;
        this.kernel = kernel;
        N = matrix.length;
        M = matrix[0].length;
        this.p = p;
    }

    public int[][] start() {
        Thread[] threads = new ColumnsThread[p];

        int start = 0;
        int end = M / p;
        int rest = M % p;

        int[][] result = new int[N][M];

        for (int i = 0; i < p; i++) {
            if (rest > 0) {
                end++;
                rest--;
            }
            threads[i] = new ColumnsThread(matrix, kernel, result, start, end);
            threads[i].start();

            start = end;
            end = start + M / p;
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

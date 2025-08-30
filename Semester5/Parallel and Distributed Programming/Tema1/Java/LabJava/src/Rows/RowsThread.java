package Rows;

import utils.Utils;

public class RowsThread extends Thread {
    private final int[][] matrix;
    private final int[][] kernel;
    private final int[][] result;

    private final int N;
    private final int M;
    private final int start;
    private final int end;

    RowsThread(int[][] matrix, int[][] kernel, int[][] result, int N, int M, int start, int end) {
        this.matrix = matrix;
        this.kernel = kernel;
        this.result = result;
        this.N = N;
        this.M = M;
        this.start = start;
        this.end = end;
    }

    public void run() {
        for (int i = this.start; i < this.end; i++) {
            for (int j = 0; j < this.M; j++) {
                this.result[i][j] = Utils.convolveAt(this.matrix, this.kernel, i, j, this.N, this.M, kernel.length);
            }
        }
    }
}

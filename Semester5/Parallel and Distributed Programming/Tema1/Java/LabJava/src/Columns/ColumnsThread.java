package Columns;

import utils.Utils;

public class ColumnsThread extends Thread{
    private final int[][] matrix;
    private final int[][] kernel;
    private final int[][] result;

    private final int N;
    private final int M;
    private final int start;
    private final int end;

    public ColumnsThread(int[][] matrix, int[][] kernel, int[][] result, int start, int end) {
        this.matrix = matrix;
        this.kernel = kernel;
        this.result = result;
        this.start = start;
        this.end = end;
        N = matrix.length;
        M = matrix[0].length;
    }

    public void run() {
        for(int i = 0; i < N; i++) {
            for(int j = start; j < end; j++) {
                this.result[i][j] = Utils.convolveAt(this.matrix, this.kernel, i, j, N, M, kernel.length);
            }
        }
    }
}

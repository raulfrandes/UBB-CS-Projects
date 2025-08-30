package Delta;

import utils.Utils;

public class DeltaThread extends Thread{
    private final int[][] matrix;
    private final int[][] kernel;
    private final int[][] result;

    private final int N;
    private final int M;
    private final int start;
    private final int end;

    public DeltaThread(int[][] matrix, int[][] kernel, int[][] result, int start, int end) {
        this.matrix = matrix;
        this.kernel = kernel;
        this.result = result;
        this.start = start;
        this.end = end;
        N = matrix.length;
        M = matrix[0].length;
    }

    public void run() {
        for (int index = start; index < end; index++) {
            int i = index / M;
            int j = index % M;
            this.result[i][j] = Utils.convolveAt(this.matrix, this.kernel, i, j, N, M, kernel.length);
        }
    }
}
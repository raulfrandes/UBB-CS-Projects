package Block;

import utils.Utils;

public class BlockThread extends Thread{
    private final int[][] matrix;
    private final int[][] kernel;
    private final int[][] result;

    private final int N;
    private final int M;
    private final int startRows;
    private final int startCols;
    private final int endRows;
    private final int endCols;

    public BlockThread(int[][] matrix, int[][] kernel, int[][] result, int startRows, int startCols, int endRows, int endCols) {
        this.matrix = matrix;
        this.kernel = kernel;
        this.result = result;
        this.startRows = startRows;
        this.startCols = startCols;
        this.endRows = endRows;
        this.endCols = endCols;
        N = matrix.length;
        M = matrix[0].length;
    }

    public void run() {
        for (int i = startRows; i < endRows; i++) {
            for (int j = startCols; j < endCols; j++) {
                this.result[i][j] = Utils.convolveAt(this.matrix, this.kernel, i, j, N, M, kernel.length);
            }
        }
    }
}

package Sequentially;

import utils.Utils;

public class SequentialMethod {
    private final int[][] matrix;
    private final int[][] kernel;
    private final int N;
    private final int M;
    private final int kernelSize;

    public SequentialMethod(int[][] matrix, int[][] kernel) {
        this.matrix = matrix;
        this.kernel = kernel;
        this.N = matrix.length;
        this.M = matrix[0].length;
        this.kernelSize = kernel.length;
    }

    public int[][] start() {
        int[][] result = new int[N][M];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                result[i][j] = Utils.convolveAt(matrix, kernel, i, j, N, M, kernelSize);
            }
        }

        return result;
    }
}

public class SequentialMethod {
    private final int[][] matrix;
    private final int[][] kernel;
    private final int N;
    private final int M;
    private final int kernelSize;

    public SequentialMethod(int[][] matrix, int[][] kernel) {
        this.matrix = matrix;
        this.kernel = kernel;
        N = matrix.length;
        M = matrix[0].length;
        kernelSize = kernel.length;
    }

    public void start() {
        int[] previousRow = new int[M];
        int[] currentRow = new int[M];
        int[] nextRow = new int[M];

        System.arraycopy(matrix[0], 0, previousRow, 0, M);

        for (int i = 0; i < N; i++) {
            System.arraycopy(matrix[i], 0, currentRow, 0, M);

            if (i < N - 1) {
                System.arraycopy(matrix[i + 1], 0, nextRow, 0, M);
            }
            else {
                System.arraycopy(matrix[i], 0, nextRow, 0, M);
            }

            for (int j = 0; j < M; j++) {
                matrix[i][j] = Utils.convolveAt(previousRow, currentRow, nextRow, kernel, i, j, N, M, kernelSize);
            }

            System.arraycopy(currentRow, 0, previousRow, 0, M);
        }
    }
}

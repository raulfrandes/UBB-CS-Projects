package Block;

public class BlockMethod {
    private final int[][] matrix;
    private final int[][] kernel;
    private final int N;
    private final int M;
    private final int p;

    public BlockMethod(int[][] matrix, int[][] kernel, int p) {
        this.matrix = matrix;
        this.kernel = kernel;
        this.N = matrix.length;
        this.M = matrix[0].length;
        this.p = p;
    }

    public int[][] start() {
        Thread[] threads = new BlockThread[p];

        int numRowBlocks = (int) Math.sqrt(p);
        int numColBlocks = p / numRowBlocks;

        int startRows = 0;
        int endRows = N / numRowBlocks;
        int restRows = N % numRowBlocks;

        int[][] result = new int[N][M];

        int threadIndex = 0;
        for (int i = 0; i < numRowBlocks; i++) {
            if (restRows > 0) {
                endRows++;
                restRows--;
            }

            int startCols = 0;
            int endCols = M / numColBlocks;
            int restCols = M % numColBlocks;

            for (int j = 0; j < numColBlocks; j++) {
                if (restCols > 0) {
                    endCols++;
                    restCols--;
                }

                threads[threadIndex] = new BlockThread(matrix, kernel, result, startRows, startCols, endRows, endCols);
                threads[threadIndex].start();

                startCols = endCols;
                endCols = startCols + M / numColBlocks;

                threadIndex++;
            }
            startRows = endRows;
            endRows = startRows + N / numRowBlocks;
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

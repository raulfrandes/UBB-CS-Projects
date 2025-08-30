import java.io.*;
import java.util.Random;

public class Utils {
    public static void generateTestData() {
        int[][] dimensions = {
                {10, 10},
                {1000, 1000},
                {10, 10000},
                {10000, 10},
                {10000, 10000}
        };

        int upperBound = 1000;

        for (int[] dim : dimensions) {
            int N = dim[0];
            int M = dim[1];
            int[][] testData = new int[N][M];
            Random rand = new Random();
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < M; j++) {
                    testData[i][j] = rand.nextInt(upperBound);
                }
            }

            String fileName = "src/TestData/matrix_" + N + "_" + M + ".txt";
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
                for (int[] row: testData) {
                    for (int val: row) {
                        bw.write(val + " ");
                    }
                    bw.newLine();
                }
            } catch (IOException e) {
                System.out.println("Error saving matrix " + N + "x" + M + ": " + e.getMessage());
            }
        }
    }

    public static int[][] readMatrixFromFile(String fileName) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            int rows = 0;
            int cols = 0;

            String line = br.readLine();
            if (line != null) {
                cols = line.trim().split("\\s+").length;
                rows++;
            }

            while (br.readLine() != null){
                rows++;
            }
            int[][] matrix = new int[rows][cols];

            try (BufferedReader br2 = new BufferedReader(new FileReader(fileName))) {
                int row = 0;
                while ((line = br2.readLine()) != null) {
                    String[] values = line.trim().split("\\s+");
                    for (int i = 0; i < cols; i++) {
                        matrix[row][i] = Integer.parseInt(values[i]);
                    }
                    row++;
                }
            }
            return matrix;
        }
    }

    public static int convolveAt(
            int[] previousRow,
            int[] currentRow,
            int[] nextRow,
            int[][] kernel,
            int i, int j,
            int N, int M, int kSize
    ) {
        int result = 0;

        for (int ki = 0; ki < kSize; ki++) {
            for (int kj = 0; kj < kSize; kj++) {
                int x = i + ki - kSize / 2;
                int y = j + kj - kSize / 2;

                int validX = Math.min(Math.max(x, 0) , N - 1);
                int validY = Math.min(Math.max(y, 0) , M - 1);

                int value;
                if (validX == i - 1) {
                    value = previousRow[validY];
                } else if (validX == i){
                    value = currentRow[validY];
                } else {
                    value = nextRow[validY];
                }

                result += value * kernel[ki][kj];
            }
        }

        return result;
    }

    public static void writeMatrixToFile(String fileName, int[][] matrix) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (int[] row: matrix) {
                for (int val: row) {
                    bw.write(val + " ");
                }
                bw.newLine();
            }
        }
    }

    public static boolean areResultFilesIdentical(String fileName) throws IOException {
        String sequentialResultFile = "Result/SequentialResult" + fileName.split("Result")[2];

        boolean same = true;
        try (BufferedReader br = new BufferedReader(new FileReader(sequentialResultFile));
             BufferedReader br2 = new BufferedReader(new FileReader(fileName))) {
            String line1, line2;

            while ((line1 = br.readLine()) != null && (line2 = br2.readLine()) != null) {
                if (!line1.trim().equals(line2.trim())) {
                    same = false;
                }
            }

            if (br.readLine() != null || br2.readLine() != null) {
                same = false;
            }
        }

        return same;
    }
}

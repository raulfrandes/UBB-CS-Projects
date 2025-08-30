import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // utils.Utils.generateTestData();

        String fileName = args[2];

        int[][] matrix = new int[0][0];
        try {
            matrix = Utils.readMatrixFromFile("TestData/" + fileName + ".txt");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        int[][] kernel = {
                {2, 1, 2},
                {1, 0, 1},
                {2, 1, 2}
        };

        String option = args[0];
        int p = Integer.parseInt(args[1]);

        String[] resultFiles = {
                "Result/SequentialResult"  + fileName.split("x")[1] + ".txt",
                "Result/RowsResult"  + fileName.split("x")[1] + ".txt"
        };

        long startTime, endTime;

        switch (option) {
            case "Sequential":
                SequentialMethod sequentialMethod = new SequentialMethod(matrix, kernel);
                startTime = System.nanoTime();
                sequentialMethod.start();
                endTime = System.nanoTime();
                System.out.println((endTime - startTime) / 1000000f);
                System.out.println();

                try {
                    Utils.writeMatrixToFile(resultFiles[0], matrix);
                } catch (IOException e) {
                    System.out.println("Error writing to file: " + e.getMessage());
                }
                break;
            case "Rows":
                RowsMethod rowsMethod = new RowsMethod(matrix, kernel, p);
                startTime = System.nanoTime();
                rowsMethod.start();
                endTime = System.nanoTime();
                System.out.println((endTime - startTime) / 1000000f);

                try {
                    Utils.writeMatrixToFile(resultFiles[1], matrix);

                    boolean result = Utils.areResultFilesIdentical(resultFiles[1]);
                    if (result) {
                        System.out.println("Identical");
                    } else {
                        System.out.println("Not identical");
                    }
                } catch (IOException e) {
                    System.out.println("Error writing to file: " + e.getMessage());
                }
                break;
        }
    }
}
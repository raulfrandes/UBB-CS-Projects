import Block.BlockMethod;
import Columns.ColumnsMethod;
import Delta.DeltaMethod;
import Rows.RowsMethod;
import Sequentially.SequentialMethod;
import utils.Utils;

import java.io.IOException;
import java.util.Arrays;

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

        int[][] kernelSize3 = {
                {2, 1, 2},
                {1, 0, 1},
                {2, 1, 2}
        };

        int[][] kernelSize5 = {
                {3, 2, 1, 2, 3},
                {2, 1, 0, 1, 2},
                {1, 0, 0, 0, 1},
                {2, 1, 0, 1, 2},
                {3, 2, 1, 2, 3}
        };

        int[][] kernel;

        if (fileName.equals("matrix_10_10")) {
            kernel = kernelSize3;
        } else {
            kernel = kernelSize5;
        }

        String option = args[0];
        int p = Integer.parseInt(args[1]);

        String[] resultFiles = {
                "Result/SequentialResult"  + fileName.split("x")[1] + ".txt",
                "Result/RowsResult"  + fileName.split("x")[1] + ".txt",
                "Result/ColumnsResult"  + fileName.split("x")[1] + ".txt",
                "Result/BlockResult"  + fileName.split("x")[1] + ".txt",
                "Result/DeltaResult"  + fileName.split("x")[1] + ".txt",
        };

        long startTime, endTime;

        switch (option) {
            case "Sequential":
                SequentialMethod sequentialMethod = new SequentialMethod(matrix, kernel);
                startTime = System.nanoTime();
                int[][] sequentiallyResult = sequentialMethod.start();
                endTime = System.nanoTime();
                System.out.println();
                System.out.println((endTime - startTime) / 1000000f);

                try {
                    Utils.writeMatrixToFile(resultFiles[0], sequentiallyResult);
                    System.out.println();
                } catch (IOException e) {
                    System.out.println("Error writing to file: " + e.getMessage());
                }
                break;
            case "Rows":
                RowsMethod rowsMethod = new RowsMethod(matrix, kernel, p);
                startTime = System.nanoTime();
                int[][] rowsResult = rowsMethod.start();
                endTime = System.nanoTime();
                System.out.println((endTime - startTime) / 1000000f);

                try {
                    Utils.writeMatrixToFile(resultFiles[1], rowsResult);

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
            case "Columns":
                ColumnsMethod columnsMethod = new ColumnsMethod(matrix, kernel, p);
                startTime = System.nanoTime();
                int[][] columnsResult = columnsMethod.start();
                endTime = System.nanoTime();
                System.out.println((endTime - startTime) / 1000000f);

                try {
                    Utils.writeMatrixToFile(resultFiles[2], columnsResult);

                    boolean result = Utils.areResultFilesIdentical(resultFiles[2]);
                    if (result) {
                        System.out.println("Identical");
                    } else {
                        System.out.println("Not identical");
                    }
                } catch (IOException e) {
                    System.out.println("Error writing to file: " + e.getMessage());
                }
                break;
            case "Block":
                BlockMethod blockMethod = new BlockMethod(matrix, kernel, p);
                startTime = System.nanoTime();
                int[][] blockResult = blockMethod.start();
                endTime = System.nanoTime();
                System.out.println((endTime - startTime) / 1000000f);

                try{
                    Utils.writeMatrixToFile(resultFiles[3], blockResult);

                    boolean result = Utils.areResultFilesIdentical(resultFiles[3]);
                    if (result) {
                        System.out.println("Identical");
                    } else {
                        System.out.println("Not identical");
                    }
                } catch (IOException e) {
                    System.out.println("Error writing to file: " + e.getMessage());
                }
                break;
            case "Delta":
                DeltaMethod deltaMethod = new DeltaMethod(matrix, kernel, p);
                startTime = System.nanoTime();
                int[][] deltaResult = deltaMethod.start();
                endTime = System.nanoTime();
                System.out.println((endTime - startTime) / 1000000f);

                try {
                    Utils.writeMatrixToFile(resultFiles[4], deltaResult);

                    boolean result = Utils.areResultFilesIdentical(resultFiles[4]);
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
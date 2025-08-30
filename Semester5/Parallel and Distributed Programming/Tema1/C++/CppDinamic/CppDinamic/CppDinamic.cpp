#include <iostream>
#include <string>
#include <chrono>
#include "Utils.h"
#include "SequentialMethod.h"
#include "RowsMethod.h"
#include "ColumnsMethod.h"
#include "BlockMethod.h"
#include "DeltaMethod.h"

using std::string;

int main(int argc, char* argv[])
{
    std::string option = argv[1];
    int p = std::atoi(argv[2]);
    std::string fileName = argv[3];

    std::vector<std::vector<int>> matrix;
    Utils::readMatrixFromFile("TestData/" + fileName + ".txt", matrix);

    std::vector<std::vector<int>> kernelSize3 = {
        {2, 1, 2},
        {1, 0, 1},
        {2, 1, 2}
    };

    std::vector<std::vector<int>> kernelSize5 = {
        {3, 2, 1, 2, 3},
        {2, 1, 0, 1, 2},
        {1, 0, 0, 0, 1},
        {2, 1, 0, 1, 2},
        {3, 2, 1, 2, 3}
    };

    std::vector<std::vector<int>> kernel;

    if (fileName == "matrix_10_10") {
        kernel = kernelSize3;
    }
    else {
        kernel = kernelSize5;
    }

    if (option == "Sequential") {
        SequentialMethod sequentialMethod(matrix, kernel);
        auto startTime = std::chrono::high_resolution_clock::now();
        std::vector<std::vector<int>> sequentialResult(matrix.size(), std::vector<int>(matrix[0].size(), 0));
        sequentialMethod.start(sequentialResult);
        auto endTime = std::chrono::high_resolution_clock::now();
        double elapsed_time_ms = std::chrono::duration<double, std::milli>(endTime - startTime).count();
        std::cout << elapsed_time_ms << std::endl;
        std::cout << std::endl;

        std::string resultFileName = "Result/SequentialResult" + fileName.substr(fileName.find("x") + 1) + ".txt";
        Utils::writeMatrixToFile(resultFileName, sequentialResult);
    }
    else if (option == "Rows") {
        RowsMethod rowsMethod(matrix, kernel, p);
        auto startTime = std::chrono::high_resolution_clock::now();
        std::vector<std::vector<int>> rowsResult(matrix.size(), std::vector<int>(matrix[0].size(), 0));
        rowsMethod.start(rowsResult);
        auto endTime = std::chrono::high_resolution_clock::now();
        double elapsed_time_ms = std::chrono::duration<double, std::milli>(endTime - startTime).count();
        std::cout << elapsed_time_ms << std::endl;

        std::string resultFileName = "Result/RowsResult" + fileName.substr(fileName.find("x") + 1) + ".txt";
        Utils::writeMatrixToFile(resultFileName, rowsResult);

        bool result = Utils::areResultFilesIdentical(resultFileName);
        if (result) {
            std::cout << "Identical" << std::endl;
        }
        else {
            std::cout << "Not identical" << std::endl;
        }
    }
    else if (option == "Columns") {
        ColumnsMethod columnsMethod(matrix, kernel, p);
        auto startTime = std::chrono::high_resolution_clock::now();
        std::vector<std::vector<int>> columnsResult(matrix.size(), std::vector<int>(matrix[0].size(), 0));
        columnsMethod.start(columnsResult);
        auto endTime = std::chrono::high_resolution_clock::now();
        double elapsed_time_ms = std::chrono::duration<double, std::milli>(endTime - startTime).count();
        std::cout << elapsed_time_ms << std::endl;

        std::string resultFileName = "Result/ColumnsResult" + fileName.substr(fileName.find("x") + 1) + ".txt";
        Utils::writeMatrixToFile(resultFileName, columnsResult);

        bool result = Utils::areResultFilesIdentical(resultFileName);
        if (result) {
            std::cout << "Identical" << std::endl;
        }
        else {
            std::cout << "Not identical" << std::endl;
        }
    }
    else if (option == "Block") {
        BlockMethod blockMethod(matrix, kernel, p);
        auto startTime = std::chrono::high_resolution_clock::now();
        std::vector<std::vector<int>> blockResult(matrix.size(), std::vector<int>(matrix[0].size(), 0));
        blockMethod.start(blockResult);
        auto endTime = std::chrono::high_resolution_clock::now();
        double elapsed_time_ms = std::chrono::duration<double, std::milli>(endTime - startTime).count();
        std::cout << elapsed_time_ms << std::endl;

        std::string resultFileName = "Result/BlockResult" + fileName.substr(fileName.find("x") + 1) + ".txt";
        Utils::writeMatrixToFile(resultFileName, blockResult);

        bool result = Utils::areResultFilesIdentical(resultFileName);
        if (result) {
            std::cout << "Identical" << std::endl;
        }
        else {
            std::cout << "Not identical" << std::endl;
        }
    }
    else if (option == "Delta") {
        DeltaMethod deltaMethod(matrix, kernel, p);
        auto startTime = std::chrono::high_resolution_clock::now();
        std::vector<std::vector<int>> deltaResult(matrix.size(), std::vector<int>(matrix[0].size(), 0));
        deltaMethod.start(deltaResult);
        auto endTime = std::chrono::high_resolution_clock::now();
        double elapsed_time_ms = std::chrono::duration<double, std::milli>(endTime - startTime).count();
        std::cout << elapsed_time_ms << std::endl;

        std::string resultFileName = "Result/DeltaResult" + fileName.substr(fileName.find("x") + 1) + ".txt";
        Utils::writeMatrixToFile(resultFileName, deltaResult);

        bool result = Utils::areResultFilesIdentical(resultFileName);
        if (result) {
            std::cout << "Identical" << std::endl;
        }
        else {
            std::cout << "Not identical" << std::endl;
        }
    }

    return 0;
}

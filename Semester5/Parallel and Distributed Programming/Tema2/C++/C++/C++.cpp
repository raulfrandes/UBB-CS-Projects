#include <iostream>
#include <vector>
#include "RowsMethod.h"
#include <chrono>
#include "SequentialMethod.h"
#include "Utils.h"

using std::string;

int main(int argc, char* argv[])
{
    std::string option = argv[1];
    int p = std::atoi(argv[2]);
    std::string fileName = argv[3];

    std::vector<std::vector<int>> matrix;
    Utils::readMatrixFromFile("TestData/" + fileName + ".txt", matrix);

    std::vector<std::vector<int>> kernel = {
        {2, 1, 2},
        {1, 0, 1},
        {2, 1, 2}
    };

    if (option == "Sequential") {
        SequentialMethod sequentialMethod(matrix, kernel);
        auto startTime = std::chrono::high_resolution_clock::now();
        sequentialMethod.start();
        auto endTime = std::chrono::high_resolution_clock::now();
        double elapsed_time_ms = std::chrono::duration<double, std::milli>(endTime - startTime).count();
        std::cout << elapsed_time_ms << std::endl;
        std::cout << std::endl;

        std::string resultFileName = "Result/SequentialResult" + fileName.substr(fileName.find("x") + 1) + ".txt";
        Utils::writeMatrixToFile(resultFileName, matrix);
    }
    else if (option == "Rows") {
        std::barrier b(p);
        RowsMethod rowsMethod(matrix, kernel, p, b);
        auto startTime = std::chrono::high_resolution_clock::now();
        rowsMethod.start();
        auto endTime = std::chrono::high_resolution_clock::now();
        double elapsed_time_ms = std::chrono::duration<double, std::milli>(endTime - startTime).count();
        std::cout << elapsed_time_ms << std::endl;

        std::string resultFileName = "Result/RowsResult" + fileName.substr(fileName.find("x") + 1) + ".txt";
        Utils::writeMatrixToFile(resultFileName, matrix);

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
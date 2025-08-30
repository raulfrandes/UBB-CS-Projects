#include <iostream>
#include <string>
#include "constants.h"
#include "Utils.h"
#include "SequentialMethod.h"
#include <chrono>
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


	int matrix[N][M] = { 0 };
	Utils::readMatrixFromFile("TestData/" + fileName + ".txt", matrix);

	int kernelSize3[3][3] = {
		{2, 1, 2},
		{1, 0, 1},
		{2, 1, 2}
	};

	int kernelSize5[5][5] = {
		{3, 2, 1, 2, 3},
		{2, 1, 0, 1, 2},
		{1, 0, 0, 0, 1},
		{2, 1, 0, 1, 2},
		{3, 2, 1, 2, 3}
	};

	int kernel[K][K];

	if (fileName == "matrix_10_10") {
		std::copy(&kernelSize3[0][0], &kernelSize3[0][0] + 3 * 3, &kernel[0][0]);
	}
	else
	{
		std::copy(&kernelSize5[0][0], &kernelSize5[0][0] + 5 * 5, &kernel[0][0]);
	}

	if (option == "Sequential") {
		SequentialMethod sequentialMethod = SequentialMethod(matrix, kernel);
		auto startTime = std::chrono::high_resolution_clock::now();
		int sequentialResult[N][M] = {0};
		sequentialMethod.start(sequentialResult);
		auto endTime = std::chrono::high_resolution_clock::now();
		double elapsed_time_ms = std::chrono::duration<double, std::milli>(endTime - startTime).count();
		std::cout << elapsed_time_ms << std::endl;
		std::cout << std::endl;

		std::string resultFileName = "Result/SequentialResult" + fileName.substr(fileName.find("x") + 1) + ".txt";
		Utils::writeMatrixToFile(resultFileName, sequentialResult);
	}
	else if (option == "Rows") {
		RowsMethod rowsMethod = RowsMethod(matrix, kernel, p);
		auto startTime = std::chrono::high_resolution_clock::now();
		int rowsResult[N][M] = { 0 };
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
		ColumnsMethod columnsMethod = ColumnsMethod(matrix, kernel, p);
		auto startTime = std::chrono::high_resolution_clock::now();
		int columnsResult[N][M] = { 0 };
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
		BlockMethod blockMethod = BlockMethod(matrix, kernel, p);
		auto startTime = std::chrono::high_resolution_clock::now();
		int blockResult[N][M] = { 0 };
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
		DeltaMethod deltaMethod = DeltaMethod(matrix, kernel, p);
		auto startTime = std::chrono::high_resolution_clock::now();
		int deltaResult[N][M] = { 0 };
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
}
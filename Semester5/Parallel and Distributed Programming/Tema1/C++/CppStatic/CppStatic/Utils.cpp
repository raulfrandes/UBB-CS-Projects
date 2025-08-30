#include "Utils.h"
#include <fstream>
#include <iostream>
#include <sstream>

using std::ifstream;
using std::cout;

void Utils::readMatrixFromFile(const string& fileName, int matrix[N][M])
{
	ifstream file(fileName);

	if (!file.is_open()) {
		cout << "Error opening file: " << fileName << std::endl;
		return;
	}

	string buffer;
	int row = 0;

	file.seekg(0, std::ios::end);
	buffer.reserve(file.tellg());
	file.seekg(0, std::ios::beg);

	buffer.assign((std::istreambuf_iterator<char>(file)), std::istreambuf_iterator<char>());
	file.close();

	std::stringstream ss(buffer);
	int value, col = 0;

	while (ss >> value && row < N) {
		matrix[row][col] = value;
		col++;
		if (col == M) {
			row++;
			col = 0;
		}
	}
}

int Utils::convolveAt(int matrix[N][M], int kernel[K][K], int i, int j)
{
	int result = 0;

	for (int ki = 0; ki < K; ki++) {
		for (int kj = 0; kj < K; kj++) {
			int x = i + ki - K / 2;
			int y = j + kj - K / 2;

			int validX = std::min(std::max(x, 0), N - 1);
			int validY = std::min(std::max(y, 0), M - 1);

			result += matrix[validX][validY] * kernel[ki][kj];
		}
	}

	return result;
}

void Utils::writeMatrixToFile(const std::string& fileName, int matrix[N][M])
{
	std::ofstream file(fileName);

	if (!file.is_open()) {
		std::cerr << "Error opening file: " << fileName << std::endl;
		return;
	}

	for (int i = 0; i < N; i++) {
		for (int j = 0; j < M; j++) {
			file << matrix[i][j] << " ";
		}
		file << '\n';
	}

	file.close();
}

bool Utils::areResultFilesIdentical(const std::string& fileName)
{
	std::string sequentialResultFile = "Result/SequentialResult" + fileName.substr(fileName.find("Result_") + 6);

	std::ifstream file1(sequentialResultFile);
	std::ifstream file2(fileName);

	if (!file1.is_open() || !file2.is_open()) {
		std::cerr << "Error opening on of the files: " << sequentialResultFile << " or " << fileName << std::endl;
		return false;
	}

	std::string line1, line2;
	while (std::getline(file1, line1) && std::getline(file2, line2)) {
		if (line1 != line2) {
			std::cout << "qwd";
			return false;
		}
	}

	return true;
}

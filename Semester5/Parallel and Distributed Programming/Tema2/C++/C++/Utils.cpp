#include "Utils.h"
#include <fstream>
#include <iostream>
#include <sstream>

using std::ifstream;
using std::cout;
using std::vector;

void Utils::readMatrixFromFile(const std::string& fileName, std::vector<std::vector<int>>& matrix)
{
    ifstream file(fileName);

    if (!file.is_open()) {
        cout << "Error opening file: " << fileName << std::endl;
        return;
    }

    std::string line;
    while (std::getline(file, line)) {
        std::stringstream ss(line);
        std::vector<int> row;
        int value;
        while (ss >> value) {
            row.push_back(value);
        }
        matrix.push_back(row);
    }

    file.close();
}

int Utils::convolveAt(const vector<int>& previousRow, const vector<int>& currentRow, const vector<int>& nextRow, const vector<vector<int>>& kernel, int i, int j, int N, int M)
{
    int result = 0;
    int K = kernel.size();

    for (int ki = 0; ki < K; ki++) {
        for (int kj = 0; kj < K; kj++) {
            int x = i + ki - K / 2;
            int y = j + kj - K / 2;

            int validX = std::min(std::max(x, 0), N - 1);
            int validY = std::min(std::max(y, 0), M - 1);

            int value;
            if (validX == i - 1) {
				value = previousRow[validY];
			}
            else if (validX == i) {
				value = currentRow[validY];
			}
            else {
				value = nextRow[validY];
            }

            result += value * kernel[ki][kj];
        }
    }

    return result;
}

void Utils::writeMatrixToFile(const std::string& fileName, const std::vector<std::vector<int>>& matrix)
{
    std::ofstream file(fileName);

    if (!file.is_open()) {
        std::cerr << "Error opening file: " << fileName << std::endl;
        return;
    }

    for (const auto& row : matrix) {
        for (const auto& val : row) {
            file << val << " ";
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
        std::cerr << "Error opening one of the files: " << sequentialResultFile << " or " << fileName << std::endl;
        return false;
    }

    std::string line1, line2;
    while (std::getline(file1, line1) && std::getline(file2, line2)) {
        if (line1 != line2) {
            return false;
        }
    }

    return true;
}
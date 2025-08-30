#include "Utils.h"
#include <random>
#include <fstream>
#include <iostream>

void Utils::generateNumbers()
{
	std::vector<std::vector<int>> dimensions = {
		{ 1000, 1000},
		{ 100, 100000}
	};

	std::random_device rd;
	std::mt19937 gen(rd());
	std::uniform_int_distribution<int> dist(0, 9);

	for (std::vector<int> dim : dimensions) {
		std::vector<int> number1;
		std::vector<int> number2;

		for (int i = 0; i < dim[0]; i++) {
			int digit1 = (i == dim[0] - 1) ? dist(gen) % 9 + 1 : dist(gen);
			number1.push_back(digit1);
		}

		for (int i = 0; i < dim[1]; i++) {
			int digit2 = (i == dim[1] - 1) ? dist(gen) % 9 + 1 : dist(gen);
			number2.push_back(digit2);
		}

		std::string fileName1 = "Numbers/Numbers_" + std::to_string(dim[0]) + "_" + std::to_string(dim[1]) + "/Number1.txt";
		std::string fileName2 = "Numbers/Numbers_" + std::to_string(dim[0]) + "_" + std::to_string(dim[1]) + "/Number2.txt";

		std::ofstream file1(fileName1);
		if (!file1) {
			std::cerr << "Error opening file " << fileName1 << std::endl;
			return;
		}
		file1 << number1.size() << std::endl;
		for (int digit : number1) {
			file1 << digit;
		}
		
		std::ofstream file2(fileName2);
		if (!file2) {
			std::cerr << "Error opening file " << fileName2 << std::endl;
			return;
		}
		file2 << number2.size() << std::endl;
		for (int digit : number2) {
			file2 << digit;
		}
	}
}

void Utils::writeNumberToFile(std::string fileName, std::vector<int>& number)
{
	std::ofstream file(fileName);
	if (!file) {
		std::cerr << "Error opening file " << fileName << std::endl;
		return;
	}

	file << number.size() << std::endl;
	for (int digit : number) {
		file << digit;
	}
}

bool Utils::areResultFilesIdentical(const std::string& fileName)
{
	std::string sequentialResultFile = "Numbers/" + fileName.substr(fileName.find("Numbers_"), fileName.length() - 24) + "/Number3Var0.txt";

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
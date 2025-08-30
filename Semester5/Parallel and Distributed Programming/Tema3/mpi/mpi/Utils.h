#pragma once
#include <string>
#include <vector>

class Utils
{
public:
	static void generateNumbers();
	static void writeNumberToFile(std::string fileName, std::vector<int>& number);
	static bool areResultFilesIdentical(const std::string& fileName);
};


#pragma once
#include <string>
#include "constants.h"

using std::string;

class Utils
{
public:
		static void readMatrixFromFile(const string &fileName, int matrix[N][M]);
		
		static int convolveAt(int matrix[N][M], int kernel[K][K], int i, int j);

		static void writeMatrixToFile(const std::string& fileName, int matrix[N][M]);

		static bool areResultFilesIdentical(const std::string& fileName);
};


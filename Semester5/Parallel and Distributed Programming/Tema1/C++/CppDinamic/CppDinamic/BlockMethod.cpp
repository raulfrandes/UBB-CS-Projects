#include "BlockMethod.h"
#include "Utils.h"
#include <thread>

void BlockMethod::task(std::vector<std::vector<int>>& result, int startRows, int startCols, int endRows, int endCols)
{
	for (int i = startRows; i < endRows; i++) {
		for (int j = startCols; j < endCols; j++) {
			result[i][j] = Utils::convolveAt(matrix, kernel, i, j);
		}
	}
}

BlockMethod::BlockMethod(const std::vector<std::vector<int>>& matrix, const std::vector<std::vector<int>>& kernel, int p) : matrix(matrix), kernel(kernel), p(p)
{}

void BlockMethod::start(std::vector<std::vector<int>>& result)
{
	int N = matrix.size();
	int M = matrix[0].size();
	std::vector<std::thread> threads(p);

	int numRowBlocks = (int)sqrt(p);
	int numColBlocks = p / numRowBlocks;

	int startRows = 0;
	int endRows = N / numRowBlocks;
	int restRows = N % numRowBlocks;

	int threadIndex = 0;
	for (int i = 0; i < numRowBlocks; i++) {
		if (restRows > 0) {
			endRows++;
			restRows--;
		}

		int startCols = 0;
		int endCols = M / numColBlocks;
		int restCols = M % numColBlocks;

		for (int j = 0; j < numColBlocks; j++) {
			if (restCols > 0) {
				endCols++;
				restCols--;
			}

			threads[threadIndex] = std::thread(&BlockMethod::task, this, std::ref(result), startRows, startCols, endRows, endCols);

			startCols = endCols;
			endCols = startCols + M / numColBlocks;
			threadIndex++;
		}

		startRows = endRows;
		endRows = startRows + N / numRowBlocks;
	}

	for (int i = 0; i < p; i++) {
		threads[i].join();
	}
}

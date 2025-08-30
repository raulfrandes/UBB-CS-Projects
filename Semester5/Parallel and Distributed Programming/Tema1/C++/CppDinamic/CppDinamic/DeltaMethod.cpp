#include "DeltaMethod.h"
#include "Utils.h"
#include <thread>

void DeltaMethod::task(std::vector<std::vector<int>>& result, int start, int end)
{
	for (int index = start; index < end; index++) {
		int i = index / matrix[0].size();
		int j = index % matrix[0].size();
		result[i][j] = Utils::convolveAt(matrix, kernel, i, j);
	}
}

DeltaMethod::DeltaMethod(const std::vector<std::vector<int>>& matrix, const std::vector<std::vector<int>>& kernel, int p) : matrix(matrix), kernel(kernel), p(p) {}

void DeltaMethod::start(std::vector<std::vector<int>>& result)
{
	int N = matrix.size();
	int M = matrix[0].size();
	std::vector<std::thread> threads(p);

	int totalElements = N * M;

	int start = 0;
	int end = totalElements / p;
	int rest = totalElements % p;

	for (int i = 0; i < p; i++) {
		if (rest > 0) {
			end++;
			rest--;
		}

		threads[i] = std::thread(&DeltaMethod::task, this, std::ref(result), start, end);

		start = end;
		end = start + totalElements / p;
	}

	for (int i = 0; i < p; i++) {
		threads[i].join();
	}
}

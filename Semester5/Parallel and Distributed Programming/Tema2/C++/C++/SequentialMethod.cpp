#include "SequentialMethod.h"
#include <sstream>
#include <algorithm>
#include "Utils.h"

SequentialMethod::SequentialMethod(std::vector<std::vector<int>>& matrix, const std::vector<std::vector<int>>& kernel): matrix(matrix), kernel(kernel){}

void SequentialMethod::start()
{
    int N = matrix.size();
    int M = matrix[0].size();
    int K = kernel.size();

	std::vector<int> previousRow(M);
	std::vector<int> currentRow(M);
	std::vector<int> nextRow(M);

	std::copy(matrix[0].begin(), matrix[0].end(), previousRow.begin());

    for (int i = 0; i < N; i++) {
		std::copy(matrix[i].begin(), matrix[i].end(), currentRow.begin());

        if (i < N - 1) {
			std::copy(matrix[i + 1].begin(), matrix[i + 1].end(), nextRow.begin());
        }
        else {
			std::copy(matrix[i].begin(), matrix[i].end(), nextRow.begin());
        }

        for (int j = 0; j < M; j++) {
			matrix[i][j] = Utils::convolveAt(previousRow, currentRow, nextRow, kernel, i, j, N, M);
        }

		std::copy(currentRow.begin(), currentRow.end(), previousRow.begin());
    }
}

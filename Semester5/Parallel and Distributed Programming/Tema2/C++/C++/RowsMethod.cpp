#include "RowsMethod.h"
#include "Utils.h"


void RowsMethod::task(int start, int end)
{
	std::vector<int> previousRow(M);
	std::vector<int> currentRow(M);
	std::vector<int> nextRow(M);
	std::vector<int> endRow(M);

    if (start > 0) {
		std::copy(matrix[start - 1].begin(), matrix[start - 1].end(), previousRow.begin());
	}
    else {
		std::copy(matrix[0].begin(), matrix[0].end(), previousRow.begin());
    }

	std::copy(matrix[start].begin(), matrix[start].end(), currentRow.begin());

    if (end < N - 1) {
		std::copy(matrix[end].begin(), matrix[end].end(), endRow.begin());
		std::copy(matrix[start + 1].begin(), matrix[start + 1].end(), nextRow.begin());
	}
	else {
		std::copy(matrix[N - 1].begin(), matrix[N - 1].end(), endRow.begin());
		std::copy(matrix[N - 1].begin(), matrix[N - 1].end(), nextRow.begin());
    }

	b.arrive_and_wait();

    for (int i = start; i < end; i++) {
        if (i > start) {
			std::copy(matrix[i].begin(), matrix[i].end(), currentRow.begin());
        }

        if (i < end - 1) {
			std::copy(matrix[i + 1].begin(), matrix[i + 1].end(), nextRow.begin());
		}
		else {
			std::copy(endRow.begin(), endRow.end(), nextRow.begin());
        }
        for (int j = 0; j < M; j++) {
            matrix[i][j] = Utils::convolveAt(previousRow, currentRow, nextRow, kernel, i, j, N, M);
        }

		std::copy(currentRow.begin(), currentRow.end(), previousRow.begin());
    }
}

RowsMethod::RowsMethod(std::vector<std::vector<int>>& matrix, const std::vector<std::vector<int>>& kernel, int p, std::barrier<>& b)
    : matrix(matrix), kernel(kernel), p(p), b(b) {
    N = matrix.size();
    M = matrix[0].size();
}

void RowsMethod::start()
{
    std::vector<std::thread> threads(p);

    int start = 0;
    int end = N / p;
    int rest = N % p;

    for (int i = 0; i < p; i++) {
        if (rest > 0) {
            end++;
            rest--;
        }

        threads[i] = std::thread(&RowsMethod::task, this, start, end);

        start = end;
        end = start + N / p;
    }

    for (int i = 0; i < p; i++) {
        threads[i].join();
    }
}

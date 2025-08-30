#include "RowsMethod.h"
#include <thread>
#include <vector>
#include "Utils.h"

void RowsMethod::task(int result[N][M], int start, int end)
{
	for (int i = start; i < end; i++) {
		for (int j = 0; j < M; j++) {
			result[i][j] = Utils::convolveAt(matrix, kernel, i, j);
		}
	}
}

void RowsMethod::start(int result[N][M])
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

		threads[i] = std::thread(&RowsMethod::task, this, std::ref(result), start, end);

		start = end;
		end = start + N / p;
	}

	for (int i = 0; i < p; i++) {
		threads[i].join();
	}
}

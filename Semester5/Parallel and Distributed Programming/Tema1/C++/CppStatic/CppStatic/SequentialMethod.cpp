#include "SequentialMethod.h"

SequentialMethod::SequentialMethod(int matrix[N][M], int kernel[K][K])
{
	this->matrix = matrix;
	this->kernel = kernel;
}

void SequentialMethod::start(int result[N][M])
{
	for (int i = 0; i < N; i++) {
		for (int j = 0; j < M; j++) {
			result[i][j] = Utils::convolveAt(matrix, kernel, i, j);
		}
	}
}

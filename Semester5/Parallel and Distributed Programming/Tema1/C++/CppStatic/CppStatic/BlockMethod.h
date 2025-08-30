#pragma once
#include "constants.h"
class BlockMethod
{
private:
	int (*matrix)[M];
	int (*kernel)[K];
	const int p;

	void task(int result[N][M], int startRows, int startCols, int endRows, int endCols);

public:
	BlockMethod(int matrix[N][M], int kernel[K][K], int p) : matrix(matrix), kernel(kernel), p(p) {}

	void start(int result[N][M]);
};


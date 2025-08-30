#pragma once
#include "constants.h"
class ColumnsMethod
{
private:
	int (*matrix)[M];
	int (*kernel)[K];
	const int p;

	void task(int result[N][M], int start, int end);

public:
	ColumnsMethod(int matrix[N][M], int kernel[K][K], int p) : matrix(matrix), kernel(kernel), p(p) {}

	void start(int result[N][M]);
};


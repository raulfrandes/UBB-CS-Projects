#pragma once
#include "constants.h"
#include "Utils.h"

class SequentialMethod {
private:
	int (*matrix)[M];
	int (*kernel)[K];

public:
	SequentialMethod(int matrix[N][M], int kernel[K][K]);

	void start(int result[N][M]);
};
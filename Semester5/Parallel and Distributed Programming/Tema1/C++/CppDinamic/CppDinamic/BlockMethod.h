#pragma once
#include <vector>
class BlockMethod
{
private:
	std::vector<std::vector<int>> matrix;
	std::vector<std::vector<int>> kernel;
	const int p;

	void task(std::vector<std::vector<int>>& result, int startRows, int startCols, int endRows, int endCols);

public:
	BlockMethod(const std::vector<std::vector<int>>& matrix, const std::vector<std::vector<int>>& kernel, int p);

	void start(std::vector<std::vector<int>>& result);
};


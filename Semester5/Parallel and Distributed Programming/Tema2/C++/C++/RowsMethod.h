#pragma once
#include <barrier>
#include <thread>
#include <vector>

class RowsMethod
{
private:
    std::vector<std::vector<int>> &matrix;
    std::vector<std::vector<int>> kernel;
    const int p;
    int N;
    int M;
	std::barrier<>& b;

    void task(int start, int end);

public:
    RowsMethod(std::vector<std::vector<int>>& matrix, const std::vector<std::vector<int>>& kernel, int p, std::barrier<>& b);

    void start();
};


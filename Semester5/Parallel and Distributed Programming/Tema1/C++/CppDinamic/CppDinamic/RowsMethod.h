#pragma once
#include <vector>

class RowsMethod
{
private:
    std::vector<std::vector<int>> matrix;
    std::vector<std::vector<int>> kernel;
    const int p;

    void task(std::vector<std::vector<int>>& result, int start, int end);

public:
    RowsMethod(const std::vector<std::vector<int>>& matrix, const std::vector<std::vector<int>>& kernel, int p);

    void start(std::vector<std::vector<int>>& result);
};

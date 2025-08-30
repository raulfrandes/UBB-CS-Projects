#pragma once
#include <vector>

class ColumnsMethod
{
private:
    std::vector<std::vector<int>> matrix;
    std::vector<std::vector<int>> kernel;
    const int p; 

    void task(std::vector<std::vector<int>>& result, int start, int end);

public:
    ColumnsMethod(const std::vector<std::vector<int>>& matrix, const std::vector<std::vector<int>>& kernel, int p);

    void start(std::vector<std::vector<int>>& result);
};

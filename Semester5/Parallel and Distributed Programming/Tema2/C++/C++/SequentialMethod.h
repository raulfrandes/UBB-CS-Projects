#pragma once
#include <vector>

class SequentialMethod
{
private:
    std::vector<std::vector<int>>& matrix;
    std::vector<std::vector<int>> kernel;

public:
    SequentialMethod(std::vector<std::vector<int>>& matrix, const std::vector<std::vector<int>>& kernel);

    void start();
};


#pragma once
#include <vector>
#include "Utils.h"

class SequentialMethod {
private:
    std::vector<std::vector<int>> matrix;
    std::vector<std::vector<int>> kernel;

public:
    SequentialMethod(const std::vector<std::vector<int>>& matrix, const std::vector<std::vector<int>>& kernel);

    void start(std::vector<std::vector<int>>& result);
};

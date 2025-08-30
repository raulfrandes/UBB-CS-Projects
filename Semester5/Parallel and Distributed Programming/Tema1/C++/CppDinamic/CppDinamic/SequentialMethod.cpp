#include "SequentialMethod.h"

SequentialMethod::SequentialMethod(const std::vector<std::vector<int>>& matrix, const std::vector<std::vector<int>>& kernel)
{
    this->matrix = matrix;
    this->kernel = kernel;
}

void SequentialMethod::start(std::vector<std::vector<int>>& result)
{
    int N = matrix.size();       
    int M = matrix[0].size();    
    int K = kernel.size();       

    for (int i = 0; i < N; i++) {
        for (int j = 0; j < M; j++) {
            result[i][j] = Utils::convolveAt(matrix, kernel, i, j);  
        }
    }
}

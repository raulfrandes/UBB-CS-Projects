#include "ColumnsMethod.h"
#include "Utils.h"
#include <vector>
#include <thread>

void ColumnsMethod::task(std::vector<std::vector<int>>& result, int start, int end)
{
    int N = matrix.size();  
    for (int i = 0; i < N; i++) {
        for (int j = start; j < end; j++) {
            result[i][j] = Utils::convolveAt(matrix, kernel, i, j); 
        }
    }
}


ColumnsMethod::ColumnsMethod(const std::vector<std::vector<int>>& matrix, const std::vector<std::vector<int>>& kernel, int p)
    : matrix(matrix), kernel(kernel), p(p) {}

void ColumnsMethod::start(std::vector<std::vector<int>>& result)
{
    int N = matrix.size();  
    int M = matrix[0].size();  
    std::vector<std::thread> threads(p); 

    int start = 0;
    int end = M / p;
    int rest = M % p;

    for (int i = 0; i < p; i++) {
        if (rest > 0) {
            end++;  
            rest--;
        }

        threads[i] = std::thread(&ColumnsMethod::task, this, std::ref(result), start, end);

        start = end;
        end = start + M / p;
    }

    for (int i = 0; i < p; i++) {
        threads[i].join();
    }
}

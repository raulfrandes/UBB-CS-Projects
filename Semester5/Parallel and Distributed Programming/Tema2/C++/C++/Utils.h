#pragma once
#include <vector>
#include <string>

using namespace std;

class Utils
{
public:
    static void readMatrixFromFile(const string& fileName, vector<vector<int>>& matrix);

    static int convolveAt(const vector<int>& previousRow, const vector<int>& currentRow, const vector<int>& nextRow, const vector<vector<int>>& kernel, int i, int j, int N, int M);

    static void writeMatrixToFile(const std::string& fileName, const vector<vector<int>>& matrix);

    static bool areResultFilesIdentical(const std::string& fileName);
};


#pragma once
#include <string>
#include <vector>  

using std::string;
using std::vector;

class Utils
{
public:
    static void readMatrixFromFile(const string& fileName, vector<vector<int>>& matrix);

    static int convolveAt(const vector<vector<int>>& matrix, const vector<vector<int>>& kernel, int i, int j);

    static void writeMatrixToFile(const std::string& fileName, const vector<vector<int>>& matrix);

    static bool areResultFilesIdentical(const std::string& fileName);
};

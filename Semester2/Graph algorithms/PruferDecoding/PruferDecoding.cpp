#include <vector>
#include <fstream>
#include <iostream>

using namespace std;

void read(char* file, vector<int>& k)
{
    ifstream fin(file);
    int n;
    fin >> n;
    k = vector<int>(n);
    for (int i = 0; i < n; ++i)
        fin >> k[i];
}

void decode(vector<int> k, vector<int>& t)
{
    int n = k.size() + 1;
    t = vector<int>(k.size() + 1, -1);
    for (int q = 0; q < n - 1; ++q)
    {
        int x = k[0];
        int y = -1;
        for (int i = 0; i < n && y == -1; ++i)
            if (std::find(k.begin(), k.end(), i) == k.end())
                y = i;
        t[y] = x;
        for (int i = 0; i < k.size() - 1; ++i)
            k[i] = k[i + 1];
        k.back() = y;
    }
}

void write(char* file, const vector<int>& t)
{
    ofstream fout(file);
    fout << t.size() << '\n';
    for (const auto& elem : t)
        fout << elem << ' ';
}



int main(int argc, char** argv)
{
    vector<int> k, t;
    read(argv[1], k);
    decode(k, t);
    write(argv[2], t);
    return 0;
}
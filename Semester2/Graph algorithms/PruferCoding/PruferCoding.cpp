#include <vector>
#include <fstream>

using namespace std;

void read(char* file, int& r, vector<int>& t)
{
    ifstream fin(file);
    int n;
    fin >> n;
    t = vector<int>(n);
    for (int i = 0; i < n; ++i)
    {
        fin >> t[i];
        if (t[i] == -1) {
            r = i;
        }
    }
    fin.close();
}

auto code(vector<int> t)
{
    vector<int> k;
    for (int q = 1; q < t.size(); ++q)
    {
        int min_leaf = -1;
        for (int i = 0; i < t.size() && min_leaf == -1; ++i)
            if (std::find(t.begin(), t.end(), i) == t.end())
                min_leaf = i;
        k.push_back(t[min_leaf]);
        t[min_leaf] = min_leaf;
    }
    return k;
}

void write(char* file, const vector<int>& k)
{
    ofstream fout(file);
    fout << k.size() << '\n';
    for (const auto& elem : k)
        fout << elem << ' ';
}

int main(int argc, char** argv)
{
    int r;
    vector<int> t;
    read(argv[1], r, t);
    auto k = code(t);
    write(argv[2], k);
    return 0;
}
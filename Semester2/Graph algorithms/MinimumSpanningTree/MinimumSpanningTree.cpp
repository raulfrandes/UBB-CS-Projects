#include <iostream>
#include <fstream>
#include <vector>
#include <tuple>
#include <algorithm>

using namespace std;

int find(vector<int>& p, vector<int>& rank, int i) {
    if (p[i] == -1) {
        return i;
    }
    return p[i] = find(p, rank, p[i]);
}

void unite(vector<int>& p, vector<int>& rank, int u, int v) {
    int s1 = find(p, rank, u);
    int s2 = find(p, rank, v);

    if (s1 != s2) {
        if (rank[s1] < rank[s2]) {
            p[s1] = s2;
        }
        else if (rank[s1] > rank[s2]) {
            p[s2] = s1;
        }
        else {
            p[s2] = s1;
            rank[s1] += 1;
        }
    }
}

int Kruskal(vector<tuple<int, int, int>> edges, vector<int>& p, vector<int>& rank, vector<tuple<int, int, int>>& tree_edges) {
    sort(edges.begin(), edges.end());
    int cost = 0;
    for (const auto& edge : edges) {
        int w = get<0>(edge);
        int u = get<1>(edge);
        int v = get<2>(edge);

        if (find(p, rank, u) != find(p, rank, v)) {
            unite(p, rank, u, v);
            cost += w;
            tree_edges.push_back(make_tuple(u, v, w));
        }
    }
    return cost;
}

int main(int argc, char** argv)
{
    ifstream fin(argv[1]);
    ofstream fout(argv[2]);

    int V, E, u, v, w;
    fin >> V >> E;
    vector<tuple<int, int, int>> edges;
    while (fin >> u >> v >> w) {
        edges.push_back(make_tuple(w, u, v));
    }

    vector<int> p(V, -1);
    vector<int> rank(V, 1);
    vector<tuple<int, int, int>> tree_edges;

    int cost = Kruskal(edges, p, rank, tree_edges);

    fout << cost << endl;
    fout << tree_edges.size() << endl;

    sort(tree_edges.begin(), tree_edges.end());
    for (int i = 0; i < tree_edges.size(); i++) {
        fout << get<0>(tree_edges[i]) << " " << get<1>(tree_edges[i]) << endl;
    }

    fin.close();
    fout.close();

    return 0;
}
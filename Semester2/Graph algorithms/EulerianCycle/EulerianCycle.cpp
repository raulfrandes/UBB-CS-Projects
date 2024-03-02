#include <iostream>
#include <fstream>
#include <vector>
#include <list>
#include <stack>
#include <set>

using namespace std;

void findEulerianCycle(vector<list<int>>& adj, vector<int>& cycle) {
    int V = adj.size();

    stack<int> st;

    set<pair<int, int>> visitedEdges;

    st.push(0);

    while (!st.empty()) {
        int u = st.top();

        if (!adj[u].empty()) {
            int v = adj[u].front();

            adj[u].pop_front();
            adj[v].remove(u);
            visitedEdges.insert({ u, v });

            st.push(v);
        }
        else {
            cycle.push_back(u);
            st.pop();
        }
    }
}

int main(int argc, char* argv[]) {
    ifstream inputFile(argv[1]);
    int V, E;
    inputFile >> V >> E;

    vector<list<int>> adj(V);

    for (int i = 0; i < E; ++i) {
        int u, v;
        inputFile >> u >> v;
        adj[u].push_back(v);
        adj[v].push_back(u);
    }

    inputFile.close();

    vector<int> eulerianCycle;
    findEulerianCycle(adj, eulerianCycle);

    ofstream outputFile(argv[2]);

    for (int i = eulerianCycle.size() - 1; i >= 0; --i)
        outputFile << eulerianCycle[i] << " ";
    outputFile << endl;

    outputFile.close();

    return 0;
}

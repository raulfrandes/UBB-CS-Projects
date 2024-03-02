#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <cstring>
#include <algorithm>

using namespace std;

const int INF = 1e9;

struct Edge {
    int to;
    int capacity;
    int flow;
    int residual_capacity() const { return capacity - flow; }
};

int fordFulkerson(vector<vector<Edge>>& graph, int source, int sink) {
    int numVertices = graph.size();
    vector<vector<int>> residual_capacity(numVertices, vector<int>(numVertices, 0));

    for (int u = 0; u < numVertices; ++u) {
        for (const Edge& edge : graph[u]) {
            residual_capacity[u][edge.to] = edge.capacity;
        }
    }

    int maxFlow = 0;

    while (true) {
        vector<int> parent(numVertices, -1);
        queue<int> q;
        q.push(source);
        parent[source] = source;

        while (!q.empty() && parent[sink] == -1) {
            int u = q.front();
            q.pop();

            for (const Edge& edge : graph[u]) {
                int v = edge.to;
                if (parent[v] == -1 && residual_capacity[u][v] > 0) {
                    parent[v] = u;
                    q.push(v);
                }
            }
        }

        if (parent[sink] == -1) break;

        int pathFlow = INF;
        for (int v = sink; v != source; v = parent[v]) {
            int u = parent[v];
            pathFlow = min(pathFlow, residual_capacity[u][v]);
        }

        for (int v = sink; v != source; v = parent[v]) {
            int u = parent[v];
            residual_capacity[u][v] -= pathFlow;
            residual_capacity[v][u] += pathFlow;
        }

        maxFlow += pathFlow;
    }

    return maxFlow;
}

int main(int argc, char* argv[]) {
    ifstream inputFile(argv[1]);

    int numVertices, numEdges;
    inputFile >> numVertices >> numEdges;

    vector<vector<Edge>> graph(numVertices);

    for (int i = 0; i < numEdges; ++i) {
        int from, to, capacity;
        inputFile >> from >> to >> capacity;
        graph[from].push_back({ to, capacity, 0 });
    }

    inputFile.close();

    int source = 0, sink = numVertices - 1;

    int maxFlow = fordFulkerson(graph, source, sink);

    ofstream outputFile(argv[2]);

    outputFile << maxFlow << endl;

    outputFile.close();

    return 0;
}

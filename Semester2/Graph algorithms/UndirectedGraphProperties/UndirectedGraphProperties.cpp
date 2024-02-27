#include <iostream>
#include <fstream>
#include <vector>
#include <climits>
using namespace std;

void getIsolatedNodes(vector<vector<int>> adj_matrix, vector<int>& isolated_nodes) {
	for (int i = 0; i < adj_matrix.size(); i++) {
		int s = 0;
		for (int j = 0; j < adj_matrix[i].size(); j++) {
			s += adj_matrix[i][j];
		}
		if (s == 0) {
			isolated_nodes.push_back(i);
		}
	}
}

void printIsolatedNodes(vector<int> isolated_nodes) {
	cout << "The isolated nodes: ";
	for (int i = 0; i < isolated_nodes.size(); i++) {
		cout << isolated_nodes[i] + 1 << " ";
	}
	cout << "\n\n";
}

void isRegular(vector<vector<int>> adj_matrix) {
	vector<int> degree;
	for (int i = 0; i < adj_matrix.size(); i++) {
		int s = 0;
		for (int j = 0; j < adj_matrix[i].size(); j++) {
			s += adj_matrix[i][j];
		}
		degree.push_back(s);
	}
	bool regular = true;
	for (int i = 1; i < degree.size(); i++) {
		if (degree[0] != degree[i]) {
			regular = false;
			break;
		}
	}
	if (regular) {
		cout << "The graph is regular.\n";
	}
	else {
		cout << "The graph is not regular.\n";
	}
	cout << "\n";
}

void getDistanceMatrix(vector<vector<int>> adj_matrix, vector<vector<int>>& dist_matrix) {
	for (int i = 0; i < adj_matrix.size(); i++) {
		for (int j = 0; j < adj_matrix[i].size(); j++) {
			if (i == j) {
				dist_matrix[i][j] = 0;
			}
			else if (adj_matrix[i][j] != 0) {
				dist_matrix[i][j] = adj_matrix[i][j];
			}
		}
	}

	for (int k = 0; k < dist_matrix.size(); k++) {
		for (int i = 0; i < dist_matrix.size(); i++) {
			for (int j = 0; j < dist_matrix.size(); j++) {
				if (dist_matrix[i][k] != INT_MAX && dist_matrix[k][j] != INT_MAX) {
					if (dist_matrix[i][j] > dist_matrix[i][k] + dist_matrix[k][j]) {
						dist_matrix[i][j] = dist_matrix[i][k] + dist_matrix[k][j];
					}
				}
			}
		}
	}
}

void printDistanceMatrix(vector<vector<int>> dist_matrix) {
	for (int i = 0; i < dist_matrix.size(); i++) {
		for (int j = 0; j < dist_matrix[i].size(); j++) {
			if (dist_matrix[i][j] == INT_MAX) {
				cout << "INF" << " ";
			}
			else {
				cout << dist_matrix[i][j] << "   ";
			}
		}
		cout << "\n";
	}
	cout << "\n";
}

void DFS(vector<vector<int>> adj_matrix, vector<bool>& visited, int v) {
	visited[v] = 1;
	for (int i = 0; i < adj_matrix.size(); i++) {
		if (adj_matrix[v][i] == 1 && visited[i] == 0) {
			DFS(adj_matrix, visited, i);
		}
	}
}

void isConnected(vector<vector<int>> adj_matrix) {
	vector<bool> visited(adj_matrix.size(), false);
	DFS(adj_matrix, visited, 0);
	for (int i = 0; i < visited.size(); i++) {
		if (visited[i] == 0) {
			cout << "The graph is not connected.\n";
			return;
		}
	}
	cout << "The graph is connected.\n";
}

int main()
{
	ifstream fin("in.txt");
	int n;

	// read from file
	fin >> n;
	vector<vector<int>> adj_matrix(n, vector<int>(n));
	for (int i = 0; i < n; i++)
		for (int j = 0; j < n; j++) {
			fin >> adj_matrix[i][j];
		}

	// the isolated nodes
	vector<int> isolated_nodes;
	getIsolatedNodes(adj_matrix, isolated_nodes);
	printIsolatedNodes(isolated_nodes);

	// verify if the graph is regular
	isRegular(adj_matrix);

	// the distance matrix
	vector<vector<int>> dist_matrix(n, vector<int>(n, INT_MAX));
	getDistanceMatrix(adj_matrix, dist_matrix);
	printDistanceMatrix(dist_matrix);

	// connectivity
	isConnected(adj_matrix);

	return 0;
}

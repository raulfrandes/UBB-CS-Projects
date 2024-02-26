#include <iostream>
#include <fstream>
#include <vector>
#include <algorithm>

using namespace std;

void getAdjacencyMatrix(vector<pair<int, int>> edges, vector<vector<int>>& adj_mat) {
	for (int i = 0; i < edges.size(); i++) {
		adj_mat[edges[i].first][edges[i].second] = 1;
		adj_mat[edges[i].second][edges[i].first] = 1;
	}
}

void printAdjacencyMatrix(vector<vector<int>> adj_mat) {
	cout << "The adjacency matrix:\n";
	for (int i = 0; i < adj_mat.size(); i++) {
		for (int j = 0; j < adj_mat[i].size(); j++) {
			cout << adj_mat[i][j] << " ";
		}
		cout << "\n";
	}
	cout << "\n";
}

void getAdjacencyList(vector<vector<int>> adj_mat, vector<vector<int>>& adj_list) {
	for (int i = 0; i < adj_mat.size(); i++) {
		for (int j = 0; j < adj_mat[i].size(); j++) {
			if (adj_mat[i][j] == 1) {
				adj_list[i].push_back(j);
			}
		}
	}
}

void printAdjacencyList(vector<vector<int>> adj_list) {
	cout << "The adjacency list:\n";
	for (int i = 0; i < adj_list.size(); i++) {
		cout << i + 1 << ": ";
		for (int j = 0; j < adj_list[i].size(); j++) {
			cout << adj_list[i][j] + 1 << " ";
		}
		cout << "\n";
	}
	cout << "\n";
}

void getIncidenceMatrix(vector<pair<int, int>> edges, vector<vector<int>>& inc_mat) {
	for (int i = 0; i < inc_mat.size(); i++) {
		for (int j = 0; j < edges.size(); j++) {
			if (i == edges[j].first || i == edges[j].second) {
				inc_mat[i][j] = 1;
			}
		}
	}
}

void printIncidenceMatrix(vector<vector<int>> inc_mat) {
	cout << "The incidence matrix:\n";
	for (int i = 0; i < inc_mat.size(); i++) {
		for (int j = 0; j < inc_mat[i].size(); j++) {
			cout << inc_mat[i][j] << " ";
		}
		cout << "\n";
	}
	cout << "\n";
}

int main()
{
	ifstream fin("in.txt");
	vector<pair<int, int>> edges;
	int n, x, y;

	// read from file
	fin >> n;
	while (fin >> x >> y) {
		edges.push_back(make_pair(x - 1, y - 1));
	}
	sort(edges.begin(), edges.end());

	// the adjacency matrix
	vector<vector<int>> adj_mat(n, vector<int>(n));
	getAdjacencyMatrix(edges, adj_mat);
	printAdjacencyMatrix(adj_mat);

	// the adjacency list
	vector<vector<int>> adj_list(n);
	getAdjacencyList(adj_mat, adj_list);
	printAdjacencyList(adj_list);

	// the incidence matrix
	vector<vector<int>> inc_mat(n, vector<int>(edges.size()));
	getIncidenceMatrix(edges, inc_mat);
	printIncidenceMatrix(inc_mat);

	return 0;
}
#include <iostream>
#include <fstream>
#include <vector>
#include <tuple>
#include <queue>
#include <crtdbg.h>
#include <climits>
using namespace std;

void moore(vector<vector<int>> list_ad, int source, vector<int>& distance, vector<int>& parent) {
	distance[source] = 0;
	for (int i = 0; i < list_ad.size(); i++) {
		if (i != source) {
			distance[i] = INT_MAX;
		}
	}

	queue<int> visited;
	visited.push(source);

	int x, y;
	while (!visited.empty()) {
		x = visited.front();
		visited.pop();
		for (int i = 0; i < list_ad[x].size(); i++) {
			y = list_ad[x][i];
			if (distance[y] == INT_MAX) {
				parent[y] = x;
				distance[y] = distance[x] + 1;
				visited.push(y);
			}
		}
	}
}

vector<int> moorePath(vector<int> distance, vector<int> parent, int destination) {
	int k = distance[destination];
	vector<int> path(k + 1, 0);

	path[k] = destination;
	while (k != 0) {
		path[k - 1] = parent[path[k]];
		k--;
	}

	return path;
}

int main()
{
	ifstream fin("graph.txt");
	int n, x, y;

	// read from file
	fin >> n;
	vector<vector<int>> adj_list(n);
	while (fin >> x >> y) {
		adj_list[x - 1].push_back(y - 1);
	}

	// Moore algorithm
	int source;
	cout << "Source node: ";
	cin >> source;
	source--;
	cout << "\n";

	vector<int> distance(n, 0);
	vector<int> parent(n, 0);
	moore(adj_list, source, distance, parent);

	int destination;
	cout << "Destination node: ";
	cin >> destination;
	destination--;
	cout << "\n";

	vector<int> path = moorePath(distance, parent, destination);

	cout << "The shortest path from the node " << source + 1 << " to the node " << destination + 1 << " is: ";
	for (int i = 0; i < path.size(); i++) {
		cout << path[i] + 1 << " ";
	}
	cout << "\n";

	fin.close();

	return 0;
}
#include <iostream>
#include <fstream>
#include <vector>
#include <queue>
#include <climits>

using namespace std;

void BFS(vector<vector<int>> list_ad, int s, vector<int>& color, vector<int>& d, vector<int>& pi) {
	for (int u = 0; u < list_ad.size(); u++) {
		if (u != s) {
			color[u] = 0;
			d[u] = INT_MAX;
			pi[u] = -1;
		}
	}

	color[s] = 1;
	d[s] = 0;
	pi[s] = -1;

	queue<int> Q;
	Q.push(s);
	int u, v;
	while (!Q.empty()) {
		u = Q.front();
		Q.pop();
		for (int i = 0; i < list_ad[u].size(); i++) {
			v = list_ad[u][i];
			if (color[v] == 0) {
				color[v] = 1;
				d[v] = d[u] + 1;
				pi[v] = u;
				Q.push(v);
				cout << "The distance from the node " << s + 1 << " to the node " << v + 1 << " is: " << d[v] << "\n";
			}
		}
		color[u] = 2;
	}
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
		adj_list[y - 1].push_back(x - 1);
	}

	vector<int> color(n);
	vector<int> d(n);
	vector<int> pi(n);

	int s;
	cout << "Source node: ";
	cin >> s;
	s--;
	BFS(adj_list, s, color, d, pi);

	fin.close();

	return 0;
}
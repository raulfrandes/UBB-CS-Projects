#include <iostream>
#include <fstream>
#include <vector>
#include <queue>

using namespace std;

void dfsVisit(vector<vector<int>> list_ad, int u, vector<int>& color, vector<int>& pi, vector<int>& d, vector<int>& f, int time) {
	time = time + 1;
	d[u] = time;
	color[u] = 1;
	int v;
	for (int i = 0; i < list_ad[u].size(); i++) {
		v = list_ad[u][i];
		if (color[v] == 0) {
			pi[v] = u;
			dfsVisit(list_ad, v, color, pi, d, f, time);
			cout << v + 1 << " ";
		}
	}
	color[u] = 2;
	time = time + 1;
	f[u] = time;
}

void dfs(vector<vector<int>> list_ad, vector<int>& color, vector<int>& pi, vector<int>& d, vector<int>& f) {
	for (int u = 0; u < list_ad.size(); u++) {
		color[u] = 0;
		pi[u] = -1;
	}
	int time = 0;
	for (int u = 0; u < list_ad.size(); u++) {
		if (color[u] == 0) {
			cout << " - " << u + 1 << " ";
			dfsVisit(list_ad, u, color, pi, d, f, time);
			cout << "\n";
		}
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
	vector<int> pi(n);
	vector<int> d(n);
	vector<int> f(n);

	cout << "The nodes discovered are:\n";
	dfs(adj_list, color, pi, d, f);
	cout << "\n";

	fin.close();

	return 0;
}